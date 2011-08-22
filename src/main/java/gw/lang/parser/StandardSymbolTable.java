package gw.lang.parser;

import gw.util.CaseInsensitiveHashMap;
import gw.util.GosuObjectUtil;
import gw.config.CommonServices;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IType;
import gw.lang.GosuShop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class StandardSymbolTable implements ISymbolTable
{
  public static final Method PRINT, NOW;

  static
  {
    try
    {
      PRINT = StandardSymbolTable.class.getMethod( "print", Object.class );
      NOW = StandardSymbolTable.class.getMethod( "now" );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private ArrayList _stackScopes;
  private LinkedList _stackPrivateGlobalScopes;

  /**
   * The sizes of the scopes are maintained in the array. Note the zeroth one
   * is reserved for the Global scope, which may or may not be meainingful
   * depending on the context e.g., plain expressions have a global scope, but
   * gosu classes don't.
   */
  private int[] _scopeSizes;
  private int _iScopeCsr;
  private IStack _stack;

  /**
   *
   */
  public StandardSymbolTable()
  {
    init();
    // The 'global' scope
    pushScope();
  }

  /**
   *
   * @param bDefineCommonSymbols
   */
  public StandardSymbolTable( boolean bDefineCommonSymbols )
  {
    init();

    if( bDefineCommonSymbols )
    {
      defineCommonSymbols();
    }

    // The 'global' scope
    pushScope();
  }

  private void init()
  {
    _stackScopes = new ArrayList();
    _stackPrivateGlobalScopes = new LinkedList();
    _scopeSizes = new int[8];
    _iScopeCsr = 0; // Assign to 0 instead of -1 to account for global scope
  }

  /**
   *
   * @param source
   */
  private StandardSymbolTable( StandardSymbolTable source )
  {
    _stackScopes = (ArrayList)source._stackScopes.clone();
    for( int i = 0; i < _stackScopes.size(); i++ )
    {
      Object obj = _stackScopes.get( i );
      IScope scope = (IScope)obj;
      //noinspection unchecked
      _stackScopes.set( i, scope.clone() );
    }

    _stackPrivateGlobalScopes = (LinkedList)source._stackPrivateGlobalScopes.clone();
    for( int i = 0; i < _stackPrivateGlobalScopes.size(); i++ )
    {
      Object obj = _stackPrivateGlobalScopes.get( i );
      IScope scope = (IScope)obj;
      //noinspection unchecked
      _stackPrivateGlobalScopes.set( i, scope.clone() );
    }

    _scopeSizes = new int[source._scopeSizes.length];
    System.arraycopy( source._scopeSizes, 0, _scopeSizes, 0, _scopeSizes.length );
  }

  //----------------------------------------------------------------------------
  // -- ISymbolTable impl --

  public ISymbol getSymbol( CharSequence name )
  {
    return getSymbol( name, _stackScopes.size() - 1 );
  }

  public void putSymbol( ISymbol symbol )
  {
    CaseInsensitiveCharSequence name = symbol.getCaseInsensitiveName();
    //noinspection unchecked
    peekScope().put( name, symbol );
    symbol.setDynamicSymbolTable( this );
  }

  //## todo: remove this method everywhere
  public ISymbol removeSymbol( CharSequence name )
  {
    for( int i = _stackScopes.size() - 1; i >= 0; i-- )
    {
      IScope scope = (IScope)_stackScopes.get( i );
      Object symbol = scope.get( name );
      if( symbol != null )
      {
        return (ISymbol)scope.remove( name );
      }
    }

    return null;
  }

  public int getTotalSymbolCount()
  {
    int count = 0;
    int iActivationScopeIndex = -1;
    for( int i = _stackScopes.size() - 1; i >= 0; i-- )
    {
      IScope scope = (IScope)_stackScopes.get( i );
      count += scope.countSymbols();
      IActivationContext activationCtx = scope.getActivationCtx();
      if( activationCtx != null && !activationCtx.isTransparent() )
      {
        iActivationScopeIndex = i;
        break;
      }
    }

    if( iActivationScopeIndex != -1 )
    {
      if( _stackPrivateGlobalScopes.size() > 0 )
      {
        IScope scope = (IScope)_stackPrivateGlobalScopes.getFirst();
        count += scope.countSymbols();
      }

      //noinspection ForLoopReplaceableByForEach
      for( int i = 0; i < _stackScopes.size(); i++ )
      {
        IScope scope = (IScope)_stackScopes.get( i );
        IActivationContext activationCtx = scope.getActivationCtx();
        if( activationCtx != null && !activationCtx.isTransparent() )
        {
          break;
        }

        count += scope.countSymbols();
      }
    }
    return count;
  }

  public Map getSymbols()
  {
    return getSymbols( _stackScopes.size() - 1, -1 );
  }

  public Map getSymbols( int iStartIndex, int iPrivateGlobalIndex )
  {
    Map symbols = new CaseInsensitiveHashMap();
    getSymbols( symbols, iStartIndex, iPrivateGlobalIndex );
    return symbols;
  }

  public int getScopeCount()
  {
    int iCount = _stackScopes.size();
    if( _stack != null )
    {
      iCount += _stack.getScopeCount();
    }
    return iCount;
  }

  public int getPrivateGlobalScopeCount()
  {
    return _stackPrivateGlobalScopes.size();
  }

  public IScope pushScope()
  {
    return addScope( createScope( null ) );
  }

  public IScope pushScope( IScope scope )
  {
    return addScope( scope );
  }

  public void pushPrivateGlobalScope( IScope scope )
  {
    //noinspection unchecked
    _stackPrivateGlobalScopes.addLast( scope );
  }

  public void popGlobalScope( IScope scope )
  {
    if( _stackPrivateGlobalScopes.getLast() == scope )
    {
      _stackPrivateGlobalScopes.removeLast();
      return;
    }

    removeGlobalScope( scope );
  }

  public IScope pushIsolatedScope( IActivationContext activationCtx )
  {
    // Mark an activation record.

    if( activationCtx == null )
    {
      throw new IllegalArgumentException( "The activation context must be non-null" );
    }
    return addScope( createScope( activationCtx ) );
  }

  public IScope popScope()
  {
    IScope scope = (IScope)_stackScopes.remove( _stackScopes.size() - 1 );
    if( scope.getActivationCtx() != null )
    {
      _scopeSizes[_iScopeCsr] = 0;
      _iScopeCsr--;
    }
    return scope;
  }

  public IScope popScope( IScope scope )
  {
    while( !_stackScopes.isEmpty() && scope != popScope() )
    {
    }
    return scope;
  }

  public List getCallStack()
  {
    List callStack = new ArrayList();
    int iSize = _stackScopes.size();
    for( int i = 0; i < iSize; i++ )
    {
      Object obj = _stackScopes.get( i );
      IScope scope = (IScope)obj;
      if( scope.getActivationCtx() != null )
      {
        //noinspection unchecked
        callStack.add( scope.getActivationCtx() );
      }
    }
    if( _stack != null )
    {
      //noinspection unchecked
      callStack.addAll( _stack.getCallStack() );
    }
    return callStack;
  }

  public ISymbolTable copy()
  {
    return new StandardSymbolTable( this );
  }

  public ISymbol getThisSymbolFromStackOrMap()
  {
    ISymbol thisSymbol = null;
    if( _stack != null )
    {
      thisSymbol = _stack.getThisSymbol();
    }
    if( thisSymbol == null )
    {
      thisSymbol = getSymbol( ISymbol.THIS );
    }
    return thisSymbol;
  }

  public void defineCommonSymbols()
  {
    pushScope( GosuShop.createCommonSymbolScope() );
  }

  public IStack getStack()
  {
    return _stack == null
           ? _stack = GosuShop.createGosuStack()
           : _stack;
    //putGlobalSymbolsIntoStack();
  }

  public boolean isSymbolWithinScope( ISymbol symToFind, IScope containingScope )
  {
    for( int i = _stackScopes.size() - 1; i >= 0; i-- )
    {
      IScope scope = (IScope)_stackScopes.get( i );
      Collection collection = scope.values();
      for( Object symbol : collection )
      {
        if( GosuObjectUtil.equals( symToFind, symbol ) )
        {
          return true;
        }
      }
      if( scope == containingScope )
      {
        break;
      }
    }
    return false;
  }

  public IScope peekIsolatedScope()
  {
    for( int i = _stackScopes.size() - 1; i > 0 ; i-- )
    {
      IScope iScope = (IScope)_stackScopes.get( i );
      if( iScope.getActivationCtx() != null && !
        iScope.getActivationCtx().isTransparent() )
      {
        return iScope;
      }
    }
    return null;
  }

  public void setCurrentIsolatedScope(int iScopeIndex) {
    if( _stack != null )
    {
      _stack.setCurrentScopeIndexForDebugger(iScopeIndex - _stackScopes.size());
    }
  }

  /**
   * For compile-time assignment of stack indexes.
   */
  public int getNextStackIndex()
  {
    ensureIsolatedScopeSizesCapacity();
    int csr = _iScopeCsr;
    return getNextStackIndexForScopeIndex( csr );
  }

  /**
   * For compile-time assignment of stack indexes.
   */
  public int getNextStackIndexForScope( IScope scope )
  {
    ensureIsolatedScopeSizesCapacity();
    int csr = scope.getCSR();
    return getNextStackIndexForScopeIndex( csr );
  }


  private int getNextStackIndexForScopeIndex( int csr )
  {
    if( _scopeSizes[csr] == 0 )
    {
      _scopeSizes[csr] = IStack.START_POS;
    }
    return _scopeSizes[csr]++;
  }


  public boolean hasIsolatedScope()
  {
    return _iScopeCsr > 0;
  }

  //----------------------------------------------------------------------------
  // -- private impl --

  private ISymbol getSymbol( CharSequence strName, int iStartIndex )
  {
    for( int i = iStartIndex; i >= 0; i-- )
    {
      Object obj = _stackScopes.get( i );
      IScope scope = (IScope)obj;
      Object symbol = scope.get( strName );
      if( symbol != null )
      {
        return (ISymbol)symbol;
      }
      IActivationContext activationCtx = scope.getActivationCtx();
      if( activationCtx != null && !activationCtx.isTransparent() )
      {
        // An activation record marker.
        // Jump to the global symbols and look there.
        return getGlobalSymbol( strName );
      }
    }

    return null;
  }

  private ISymbol getGlobalSymbol( CharSequence strName )
  {
    int iSize = _stackScopes.size();
    for( int i = 0; i < iSize; i++ )
    {
      // The first activation record marks the floor of global symbols
      Object obj = _stackScopes.get( i );
      IScope scope = (IScope)obj;
      IActivationContext activationCtx = scope.getActivationCtx();
      if( activationCtx != null && !activationCtx.isTransparent() )
      {
        ISymbol privateGlobal = getPrivateGlobalSymbol( strName );
        if( privateGlobal != null )
        {
          return privateGlobal;
        }

        int iGlobalFloor = i - 1;
        if( iGlobalFloor >= 0 )
        {
          return getSymbol( strName, iGlobalFloor );
        }
        else
        {
          return null;
        }
      }
    }

    return null;
  }

  private ISymbol getPrivateGlobalSymbol( CharSequence strName )
  {
    if( _stackPrivateGlobalScopes.isEmpty() )
    {
      return null;
    }

    IScope scope = (IScope)_stackPrivateGlobalScopes.getLast();
    return (ISymbol)scope.get( strName );
  }

  private IScope addScope( IScope scope )
  {
    _stackScopes.add( scope );
    if( scope.getActivationCtx() != null )
    {
      _iScopeCsr++;
      scope.setCSR( _iScopeCsr );
    }
    return scope;
  }

  private IScope createScope( IActivationContext activationCtx )
  {
    return new StandardScope( activationCtx, 8 );
  }

  private void removeGlobalScope( IScope globalScope )
  {
    int iIndex = getIndexOfGlobalScope( globalScope );
    _stackScopes.remove( iIndex - 1 );
  }

  private int getIndexOfGlobalScope( IScope globalScope )
  {
    int iIndex = getInsertionIndexOfGlobalScope();
    IScope scope = (IScope)_stackScopes.get( iIndex - 1 );
    if( scope != globalScope )
    {
      throw new IllegalArgumentException( "Cannot remove the specified global scope " +
                                          "because it is different than the global scope " +
                                          "currently occupying the top of the global scope stack." );
    }
    return iIndex;
  }

  private int getInsertionIndexOfGlobalScope()
  {
    int iSize = _stackScopes.size();
    int i;
    for( i = 0; i < iSize; i++ )
    {
      // The first activation record marks the floor of global symbols
      Object obj = _stackScopes.get( i );
      IScope scope = (IScope)obj;
      IActivationContext activationCtx = scope.getActivationCtx();
      if( activationCtx != null && !activationCtx.isTransparent() )
      {
        break;
      }
    }
    return i;
  }

  private void getSymbols( Map symbols, int iStartIndex, int iPrivateGlobalIndex )
  {
    if( iStartIndex < 0 )
    {
      iStartIndex = _stackScopes.size() - 1;
    }

    if( _stack != null && iStartIndex >= _stackScopes.size() )
    {
      int iStackStartIndex = iStartIndex - _stackScopes.size();
      Map stackSymbols = _stack.getSymbols( iStackStartIndex, iPrivateGlobalIndex );
      symbols.putAll( stackSymbols );
      iStartIndex = _stackScopes.size() - 1;
    }
    for( int i = iStartIndex; i >= 0; i-- )
    {
      Object obj = _stackScopes.get( i );
      IScope scope = (IScope)obj;
      symbols.putAll( scope );
      IActivationContext activationCtx = scope.getActivationCtx();
      if( activationCtx != null && !activationCtx.isTransparent() )
      {
        getGlobalSymbols( symbols, iPrivateGlobalIndex );
        return;
      }
    }
  }

  private void getGlobalSymbols( Map symbols, int iPrivateGlobalIndex )
  {
    int iSize = _stackScopes.size();
    for( int i = 0; i < iSize; i++ )
    {
      // The first activation record marks the floor of global symbols
      Object obj = _stackScopes.get( i );
      IScope scope = (IScope)obj;
      IActivationContext activationCtx = scope.getActivationCtx();
      if( activationCtx != null && !activationCtx.isTransparent() )
      {
        getPrivateGlobalSymbols( symbols, iPrivateGlobalIndex );

        int iGlobalFloor = i - 1;
        if( iGlobalFloor >= 0 )
        {
          getSymbols( symbols, iGlobalFloor, iPrivateGlobalIndex );
        }
        return;
      }
    }
  }

  private void getPrivateGlobalSymbols( Map symbols, int iPrivateGlobalIndex )
  {
    if( _stackPrivateGlobalScopes.isEmpty() )
    {
      return;
    }
    if( iPrivateGlobalIndex < 0 )
    {
      return;
    }
    IScope scope = (IScope)_stackPrivateGlobalScopes.get( iPrivateGlobalIndex );
    symbols.putAll( scope );
  }

  public IScope peekScope()
  {
    return (IScope)_stackScopes.get( _stackScopes.size() - 1 );
  }

  private void ensureIsolatedScopeSizesCapacity()
  {
    if( _iScopeCsr < _scopeSizes.length )
    {
      return;
    }

    int[] oldStack = _scopeSizes;
    int iNewSize = (_iScopeCsr * 3) / 2 + 1;
    _scopeSizes = new int[iNewSize];
    System.arraycopy( oldStack, 0, _scopeSizes, 0, oldStack.length );
  }

  //----------------------------------------------------------------------------
  // -- Test Functions --

  public static String now()
  {
    Date now = new Date();
    return CommonServices.getCoercionManager().formatDate( now, "medium" ) + " " +
           CommonServices.getCoercionManager().formatTime( now, "short" );
  }

  public static void print( Object obj )
  {
    System.out.println( toString( obj ) );
  }

  public static String toString( Object obj ) {
    if ( obj == null )
    {
      return "null";
    }
    if ( obj instanceof Byte )
    {
      int value = (Byte) obj;
      if ( value < 0 ) {
        value = 256 + value;
      }
      return "0x" + Integer.toHexString( value );
    }
    IType type = TypeSystem.getFromObject( obj );
    if ( type.isArray() )
    {
      StringBuilder sb = new StringBuilder();
      sb.append( '[' );
      int arrayLength = type.getArrayLength(obj);
      for ( int idx = 0; idx < arrayLength; idx++ )
      {
        if ( idx > 0 )
        {
          sb.append( ", " );
        }
        sb.append( toString( type.getArrayComponent( obj, idx ) ) );
      }
      sb.append( ']' );
      return sb.toString();
    }
    return obj.toString();
  }

  public static void error( Object strError )
  {
    System.out.println( strError );
    throw new Error( String.valueOf( strError ) );
  }

}
