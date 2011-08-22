package gw.lang.parser;

import java.util.List;
import java.util.Map;

/**
 * Ensures thread safety by delegating to a subclass-defined thread-local
 * symbol table. When not defined, delegates to the StandardSymbolTable.
 * <p/>
 * Note that although Gosu is not inherently threadsafe, it's policy is such
 * that all the runtime state must be maintained in the symbol table. Given this
 * fact a compiled Gosu unit can execute in a threadsafe manner by
 * maintaining the symbol table associated with it in thread-local storage.
 * <p/>
 * Thus thread-local symbol table allows us to cache Gosu compilation units
 * and execute them freely in a multithreaded invironment. For instance,
 * compiled Gosu rulesets, libraries, expressions, and templates can all be
 * cached provided the symbol table used to compile them is a subclass of this
 * here thread safe symbol table.
 * <p/>
 * All your subclass needs to do is provide its runtime symbol table (as apposed
 * to the compile time symbol table which is this). Simply instantiate your
 * runtime symbol table (likely an instance of StandardSymbolTable) and stuff it
 * into a static or singleton ThreadLocal. Then expose it via implementing the
 * abstract <code>getThreadLocalSymbolTable()</code> method defined here.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class ThreadSafeSymbolTable implements ISymbolTable
{
  private ISymbolTable _defaultSymTable;


  public ThreadSafeSymbolTable( boolean bDefineCommonSymbols )
  {
    _defaultSymTable = new StandardSymbolTable( bDefineCommonSymbols );
  }

  public ISymbolTable copy()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.copy();
  }

  public ISymbol getSymbol( CharSequence name )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getSymbol( name );
  }

  public void putSymbol( ISymbol symbol )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    targetSymTable.putSymbol( symbol );
    symbol.setDynamicSymbolTable( this );
  }

  public ISymbol removeSymbol( CharSequence name )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.removeSymbol( name );
  }

  public Map getSymbols()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getSymbols();
  }

  public Map getSymbols( int iScopeOffset, int iPrivateGlobalIndex )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getSymbols( iScopeOffset, iPrivateGlobalIndex );
  }

  public int getTotalSymbolCount()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getTotalSymbolCount();
  }

  public int getScopeCount()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getScopeCount();
  }

  public int getPrivateGlobalScopeCount()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getPrivateGlobalScopeCount();
  }

  public IScope pushScope()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.pushScope();
  }

  public IScope pushScope( IScope scope )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.pushScope( scope );
  }

  public void pushPrivateGlobalScope( IScope scope )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    targetSymTable.pushPrivateGlobalScope( scope );
  }

  public void popGlobalScope( IScope scope )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    targetSymTable.popGlobalScope( scope );
  }

  public IScope popScope()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.popScope();
  }

  public IScope peekScope()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.peekScope();
  }

  public IScope popScope( IScope scope )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.popScope( scope );
  }

  public IScope pushIsolatedScope( IActivationContext activationCtx )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.pushIsolatedScope( activationCtx );
  }

  public List getCallStack()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getCallStack();
  }

  public void defineCommonSymbols()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    targetSymTable.defineCommonSymbols();
  }

  public int getNextStackIndex()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getNextStackIndex();
  }

  public int getNextStackIndexForScope( IScope scope )
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getNextStackIndexForScope( scope );
  }

  public boolean hasIsolatedScope()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.hasIsolatedScope();
  }

  public IStack getStack()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getStack();
  }

  public ISymbol getThisSymbolFromStackOrMap()
  {
    ISymbolTable targetSymTable = getTargetSymbolTable();
    return targetSymTable.getThisSymbolFromStackOrMap();
  }

  public ISymbolTable getTargetSymbolTable()
  {
    ISymbolTable threadLocalSymTable = getThreadLocalSymbolTable();
    if( threadLocalSymTable != null )
    {
      return threadLocalSymTable;
    }
    else
    {
      throw new RuntimeException( "Thread-local symbol table is null" );
    }
  }

  public boolean isSymbolWithinScope( ISymbol sym, IScope scope )
  {
    ISymbolTable symbolTable = getTargetSymbolTable();
    return symbolTable.isSymbolWithinScope( sym, scope );
  }

  public IScope peekIsolatedScope()
  {
    ISymbolTable symbolTable = getTargetSymbolTable();
    return symbolTable.peekIsolatedScope();    
  }

  public void setCurrentIsolatedScope(int iScopeIndex) {
    ISymbolTable symbolTable = getTargetSymbolTable();
    symbolTable.setCurrentIsolatedScope(iScopeIndex);
  }

  protected ISymbolTable getDefaultSymbolTable()
  {
    return _defaultSymTable;
  }

  public void clearDefaultSymbolTable()
  {
    _defaultSymTable = new StandardSymbolTable( false );
  }

  /**
   * Get a thread-local symbol table. This is typically an instance of
   * StandardSymbol table you maintain in a simple ThreadLocal.
   */
  protected abstract ISymbolTable getThreadLocalSymbolTable();
}
