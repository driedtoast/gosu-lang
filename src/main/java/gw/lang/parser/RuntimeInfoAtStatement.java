package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.GosuShop;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IModule;
import gw.util.Stack;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@SuppressWarnings({"UnusedDeclaration"})
public final class RuntimeInfoAtStatement implements Cloneable
{
  private static final ThreadLocal<Stack<RuntimeInfoAtStatement>> FRAMES = new ThreadLocal<Stack<RuntimeInfoAtStatement>>();

  private RuntimeInfoAtStatement _origin;
  private ICompilableType _enclosingType;
  private String _strEnclosingFunction;
  private int _lineNumber;
  private List<ISymbol> _symbols;

  public RuntimeInfoAtStatement()
  {
    _symbols = new ArrayList<ISymbol>( 2 );
    _origin = this;
  }

  public RuntimeInfoAtStatement copy()
  {
    RuntimeInfoAtStatement copy = new RuntimeInfoAtStatement();
    copy._origin = _origin;
    copy._enclosingType = _enclosingType;
    copy._strEnclosingFunction = _strEnclosingFunction;
    copy._lineNumber = _lineNumber;
    copy._symbols = _symbols;
    return copy;
  }

  public RuntimeInfoAtStatement getOrigin()
  {
    return _origin;
  }
  
  public void setupCtx( String strEnclosingType, String strModule, String strFuncName, int iLineNum )
  {
    setEnclosingType( (ICompilableType)TypeSystem.getByFullName( strEnclosingType, strModule ) );
    setEnclosingFunction( strFuncName );
    setLineNumber( iLineNum );
    clearLocalSymbols();
  }

  private ISymbol createSymbol( String strName, IType type, Object value )
  {
    return GosuShop.createSymbol( strName, type, value );
  }

  private IType getTypeFromName( String strType )
  {
    try
    {
      IType type = null;
      for( int i = 0; i < " <>[]?:(),".length(); i++ )
      {
        if( strType.indexOf( " <>[]?:(),".charAt( i ) ) >= 0 )
        {
          // Must parse the type eg., parameterized type
          type = TypeSystem.parseType( strType, TypeVarToTypeMap.EMPTY_MAP );
        }
      }
      if( type == null )
      {
        // Simple types resolved directly by name
        IModule module = _enclosingType.getTypeLoader().getModule();
        type = TypeSystem.getByFullName( strType, module == null ? null : module.getName() );
      }
      return type;
    }
    catch( Throwable t )
    {
      return IJavaType.OBJECT;
    }
  }

  public ICompilableType getEnclosingType()
  {
    return _enclosingType;
  }
  public void setEnclosingType( ICompilableType currentContext )
  {
    _enclosingType = currentContext;
  }

  public String getEnclosingFunction()
  {
    return _strEnclosingFunction;
  }
  public void setEnclosingFunction( String strFunction )
  {
    _strEnclosingFunction = strFunction;
  }

  public int getLineNumber()
  {
    return _lineNumber;
  }
  public void setLineNumber( int lineNumber )
  {
    _lineNumber = lineNumber;
  }

  public List<ISymbol> getLocalSymbols()
  {
    return _symbols;
  }
  public void addLocalSymbol( String name, IType type, Object value )
  {
    _symbols.add( GosuShop.createSymbol( name, type, value ) );
  }
  public void clearLocalSymbols()
  {
    _symbols.clear();
  }

  public static RuntimeInfoAtStatement getCtx()
  {
    Stack<RuntimeInfoAtStatement> stack = getCallStack();
    if( stack.isEmpty() )
    {
      return null;
    }
    return stack.peek();
  }

  public static Stack<RuntimeInfoAtStatement> getCallStack()
  {
    return FRAMES.get();
  }

  public static RuntimeInfoAtStatement pushFrame()
  {
    Stack<RuntimeInfoAtStatement> stack = FRAMES.get();
    if( stack == null )
    {
      stack = new Stack<RuntimeInfoAtStatement>();
      FRAMES.set( stack );
    }
    RuntimeInfoAtStatement frame = new RuntimeInfoAtStatement();
    stack.push( frame );
    return frame;
  }

  public static RuntimeInfoAtStatement popFrame()
  {
    Stack<RuntimeInfoAtStatement> stack = FRAMES.get();
    return stack.pop();
  }

  @Override
  public RuntimeInfoAtStatement clone() {
    try {
      RuntimeInfoAtStatement context = (RuntimeInfoAtStatement) super.clone();
      context._symbols = new ArrayList<ISymbol>( _symbols );
      return context;
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  public String getStackDisplay()
  {
    return _enclosingType.getName() + "." + getEnclosingFunction();
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( o == null || getClass() != o.getClass() )
    {
      return false;
    }

    RuntimeInfoAtStatement that = (RuntimeInfoAtStatement)o;

    if( _lineNumber != that._lineNumber )
    {
      return false;
    }
    if( _enclosingType != null ? !_enclosingType.equals( that._enclosingType ) : that._enclosingType != null )
    {
      return false;
    }
    if( _strEnclosingFunction != null ? !_strEnclosingFunction.equals( that._strEnclosingFunction ) : that._strEnclosingFunction != null )
    {
      return false;
    }
    if( _symbols != null ? !_symbols.equals( that._symbols ) : that._symbols != null )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = _enclosingType != null ? _enclosingType.hashCode() : 0;
    result = 31 * result + (_strEnclosingFunction != null ? _strEnclosingFunction.hashCode() : 0);
    result = 31 * result + _lineNumber;
    result = 31 * result + (_symbols != null ? _symbols.hashCode() : 0);
    return result;
  }

  /*
    Generated methods to avoid overly massive instrumentation code for debugging. We could
    have used a single method with an array for the params, but that would require that the
    instrumented bytecode deal with array creation etc. which is to heavy -- we risk overflowing
    the 64k limit in methods when debugging from studio.

    Genterated from:

    uses java.lang.StringBuilder

    var sb = new StringBuilder()
    for( n in 1..84 )
    {
      var params = ""
      for( m in 1..n )
      {
        params += "String name${m}, String type${m}, Object value${m}"
        if( m != n )
        {
          params += ",\n                                 "
          if( n >= 10 )
          {
            params += " "     }
        }
      }
      var calls = ""
      for( c in 1..n )
      {
        calls += "    _symbols.add( createSymbol( name${c}, typeFromValue( type${c}, value${c} ), value${c} ) );\n"
      }
      var method =
        "  public void addLocalSymbols_${n}( ${params} )\n" +
        "  {\n" +
        "${calls}" +
        "  }\n\n"
        sb.append( method )
    }
    print( sb )
  */

  private IType typeFromValue( String strType, Object value )
  {
    IType type;
    if( value == null ||
        !strType.contains( "." ) ) // crappy, but fast, way to handle primitives
    {
      type = getTypeFromName( strType );
    }
    else
    {
      type = TypeSystem.getFromObject( value );
    }
    return type;
  }
  
  public void addLocalSymbols_1( String name1, String type1, Object value1 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
  }

  public void addLocalSymbols_2( String name1, String type1, Object value1,
                                 String name2, String type2, Object value2 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
  }

  public void addLocalSymbols_3( String name1, String type1, Object value1,
                                 String name2, String type2, Object value2,
                                 String name3, String type3, Object value3 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
  }

  public void addLocalSymbols_4( String name1, String type1, Object value1,
                                 String name2, String type2, Object value2,
                                 String name3, String type3, Object value3,
                                 String name4, String type4, Object value4 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
  }

  public void addLocalSymbols_5( String name1, String type1, Object value1,
                                 String name2, String type2, Object value2,
                                 String name3, String type3, Object value3,
                                 String name4, String type4, Object value4,
                                 String name5, String type5, Object value5 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
  }

  public void addLocalSymbols_6( String name1, String type1, Object value1,
                                 String name2, String type2, Object value2,
                                 String name3, String type3, Object value3,
                                 String name4, String type4, Object value4,
                                 String name5, String type5, Object value5,
                                 String name6, String type6, Object value6 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
  }

  public void addLocalSymbols_7( String name1, String type1, Object value1,
                                 String name2, String type2, Object value2,
                                 String name3, String type3, Object value3,
                                 String name4, String type4, Object value4,
                                 String name5, String type5, Object value5,
                                 String name6, String type6, Object value6,
                                 String name7, String type7, Object value7 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
  }

  public void addLocalSymbols_8( String name1, String type1, Object value1,
                                 String name2, String type2, Object value2,
                                 String name3, String type3, Object value3,
                                 String name4, String type4, Object value4,
                                 String name5, String type5, Object value5,
                                 String name6, String type6, Object value6,
                                 String name7, String type7, Object value7,
                                 String name8, String type8, Object value8 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
  }

  public void addLocalSymbols_9( String name1, String type1, Object value1,
                                 String name2, String type2, Object value2,
                                 String name3, String type3, Object value3,
                                 String name4, String type4, Object value4,
                                 String name5, String type5, Object value5,
                                 String name6, String type6, Object value6,
                                 String name7, String type7, Object value7,
                                 String name8, String type8, Object value8,
                                 String name9, String type9, Object value9 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
  }

  public void addLocalSymbols_10( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
  }

  public void addLocalSymbols_11( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
  }

  public void addLocalSymbols_12( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
  }

  public void addLocalSymbols_13( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
  }

  public void addLocalSymbols_14( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
  }

  public void addLocalSymbols_15( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
  }

  public void addLocalSymbols_16( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
  }

  public void addLocalSymbols_17( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
  }

  public void addLocalSymbols_18( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
  }

  public void addLocalSymbols_19( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
  }

  public void addLocalSymbols_20( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
  }

  public void addLocalSymbols_21( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
  }

  public void addLocalSymbols_22( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
  }

  public void addLocalSymbols_23( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
  }

  public void addLocalSymbols_24( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
  }

  public void addLocalSymbols_25( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
  }

  public void addLocalSymbols_26( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
  }

  public void addLocalSymbols_27( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
  }

  public void addLocalSymbols_28( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
  }

  public void addLocalSymbols_29( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
  }

  public void addLocalSymbols_30( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
  }

  public void addLocalSymbols_31( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
  }

  public void addLocalSymbols_32( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
  }

  public void addLocalSymbols_33( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
  }

  public void addLocalSymbols_34( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
  }

  public void addLocalSymbols_35( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
  }

  public void addLocalSymbols_36( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
  }

  public void addLocalSymbols_37( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
  }

  public void addLocalSymbols_38( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
  }

  public void addLocalSymbols_39( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
  }

  public void addLocalSymbols_40( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
  }

  public void addLocalSymbols_41( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
  }

  public void addLocalSymbols_42( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
  }

  public void addLocalSymbols_43( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
  }

  public void addLocalSymbols_44( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
  }

  public void addLocalSymbols_45( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
  }

  public void addLocalSymbols_46( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
  }

  public void addLocalSymbols_47( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
  }

  public void addLocalSymbols_48( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
  }

  public void addLocalSymbols_49( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
  }

  public void addLocalSymbols_50( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
  }

  public void addLocalSymbols_51( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
  }

  public void addLocalSymbols_52( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
  }

  public void addLocalSymbols_53( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
  }

  public void addLocalSymbols_54( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
  }

  public void addLocalSymbols_55( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
  }

  public void addLocalSymbols_56( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
  }

  public void addLocalSymbols_57( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
  }

  public void addLocalSymbols_58( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
  }

  public void addLocalSymbols_59( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
  }

  public void addLocalSymbols_60( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
  }

  public void addLocalSymbols_61( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
  }

  public void addLocalSymbols_62( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
  }

  public void addLocalSymbols_63( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
  }

  public void addLocalSymbols_64( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
  }

  public void addLocalSymbols_65( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
  }

  public void addLocalSymbols_66( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
  }

  public void addLocalSymbols_67( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
  }

  public void addLocalSymbols_68( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
  }

  public void addLocalSymbols_69( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
  }

  public void addLocalSymbols_70( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69,
                                  String name70, String type70, Object value70 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
    _symbols.add( createSymbol( name70, typeFromValue( type70, value70 ), value70 ) );
  }

  public void addLocalSymbols_71( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69,
                                  String name70, String type70, Object value70,
                                  String name71, String type71, Object value71 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
    _symbols.add( createSymbol( name70, typeFromValue( type70, value70 ), value70 ) );
    _symbols.add( createSymbol( name71, typeFromValue( type71, value71 ), value71 ) );
  }

  public void addLocalSymbols_72( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69,
                                  String name70, String type70, Object value70,
                                  String name71, String type71, Object value71,
                                  String name72, String type72, Object value72 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
    _symbols.add( createSymbol( name70, typeFromValue( type70, value70 ), value70 ) );
    _symbols.add( createSymbol( name71, typeFromValue( type71, value71 ), value71 ) );
    _symbols.add( createSymbol( name72, typeFromValue( type72, value72 ), value72 ) );
  }

  public void addLocalSymbols_73( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69,
                                  String name70, String type70, Object value70,
                                  String name71, String type71, Object value71,
                                  String name72, String type72, Object value72,
                                  String name73, String type73, Object value73 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
    _symbols.add( createSymbol( name70, typeFromValue( type70, value70 ), value70 ) );
    _symbols.add( createSymbol( name71, typeFromValue( type71, value71 ), value71 ) );
    _symbols.add( createSymbol( name72, typeFromValue( type72, value72 ), value72 ) );
    _symbols.add( createSymbol( name73, typeFromValue( type73, value73 ), value73 ) );
  }

  public void addLocalSymbols_74( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69,
                                  String name70, String type70, Object value70,
                                  String name71, String type71, Object value71,
                                  String name72, String type72, Object value72,
                                  String name73, String type73, Object value73,
                                  String name74, String type74, Object value74 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
    _symbols.add( createSymbol( name70, typeFromValue( type70, value70 ), value70 ) );
    _symbols.add( createSymbol( name71, typeFromValue( type71, value71 ), value71 ) );
    _symbols.add( createSymbol( name72, typeFromValue( type72, value72 ), value72 ) );
    _symbols.add( createSymbol( name73, typeFromValue( type73, value73 ), value73 ) );
    _symbols.add( createSymbol( name74, typeFromValue( type74, value74 ), value74 ) );
  }

  public void addLocalSymbols_75( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69,
                                  String name70, String type70, Object value70,
                                  String name71, String type71, Object value71,
                                  String name72, String type72, Object value72,
                                  String name73, String type73, Object value73,
                                  String name74, String type74, Object value74,
                                  String name75, String type75, Object value75 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
    _symbols.add( createSymbol( name70, typeFromValue( type70, value70 ), value70 ) );
    _symbols.add( createSymbol( name71, typeFromValue( type71, value71 ), value71 ) );
    _symbols.add( createSymbol( name72, typeFromValue( type72, value72 ), value72 ) );
    _symbols.add( createSymbol( name73, typeFromValue( type73, value73 ), value73 ) );
    _symbols.add( createSymbol( name74, typeFromValue( type74, value74 ), value74 ) );
    _symbols.add( createSymbol( name75, typeFromValue( type75, value75 ), value75 ) );
  }

  public void addLocalSymbols_76( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69,
                                  String name70, String type70, Object value70,
                                  String name71, String type71, Object value71,
                                  String name72, String type72, Object value72,
                                  String name73, String type73, Object value73,
                                  String name74, String type74, Object value74,
                                  String name75, String type75, Object value75,
                                  String name76, String type76, Object value76 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
    _symbols.add( createSymbol( name70, typeFromValue( type70, value70 ), value70 ) );
    _symbols.add( createSymbol( name71, typeFromValue( type71, value71 ), value71 ) );
    _symbols.add( createSymbol( name72, typeFromValue( type72, value72 ), value72 ) );
    _symbols.add( createSymbol( name73, typeFromValue( type73, value73 ), value73 ) );
    _symbols.add( createSymbol( name74, typeFromValue( type74, value74 ), value74 ) );
    _symbols.add( createSymbol( name75, typeFromValue( type75, value75 ), value75 ) );
    _symbols.add( createSymbol( name76, typeFromValue( type76, value76 ), value76 ) );
  }

  public void addLocalSymbols_77( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69,
                                  String name70, String type70, Object value70,
                                  String name71, String type71, Object value71,
                                  String name72, String type72, Object value72,
                                  String name73, String type73, Object value73,
                                  String name74, String type74, Object value74,
                                  String name75, String type75, Object value75,
                                  String name76, String type76, Object value76,
                                  String name77, String type77, Object value77 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
    _symbols.add( createSymbol( name70, typeFromValue( type70, value70 ), value70 ) );
    _symbols.add( createSymbol( name71, typeFromValue( type71, value71 ), value71 ) );
    _symbols.add( createSymbol( name72, typeFromValue( type72, value72 ), value72 ) );
    _symbols.add( createSymbol( name73, typeFromValue( type73, value73 ), value73 ) );
    _symbols.add( createSymbol( name74, typeFromValue( type74, value74 ), value74 ) );
    _symbols.add( createSymbol( name75, typeFromValue( type75, value75 ), value75 ) );
    _symbols.add( createSymbol( name76, typeFromValue( type76, value76 ), value76 ) );
    _symbols.add( createSymbol( name77, typeFromValue( type77, value77 ), value77 ) );
  }

  public void addLocalSymbols_78( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69,
                                  String name70, String type70, Object value70,
                                  String name71, String type71, Object value71,
                                  String name72, String type72, Object value72,
                                  String name73, String type73, Object value73,
                                  String name74, String type74, Object value74,
                                  String name75, String type75, Object value75,
                                  String name76, String type76, Object value76,
                                  String name77, String type77, Object value77,
                                  String name78, String type78, Object value78 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
    _symbols.add( createSymbol( name70, typeFromValue( type70, value70 ), value70 ) );
    _symbols.add( createSymbol( name71, typeFromValue( type71, value71 ), value71 ) );
    _symbols.add( createSymbol( name72, typeFromValue( type72, value72 ), value72 ) );
    _symbols.add( createSymbol( name73, typeFromValue( type73, value73 ), value73 ) );
    _symbols.add( createSymbol( name74, typeFromValue( type74, value74 ), value74 ) );
    _symbols.add( createSymbol( name75, typeFromValue( type75, value75 ), value75 ) );
    _symbols.add( createSymbol( name76, typeFromValue( type76, value76 ), value76 ) );
    _symbols.add( createSymbol( name77, typeFromValue( type77, value77 ), value77 ) );
    _symbols.add( createSymbol( name78, typeFromValue( type78, value78 ), value78 ) );
  }

  public void addLocalSymbols_79( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69,
                                  String name70, String type70, Object value70,
                                  String name71, String type71, Object value71,
                                  String name72, String type72, Object value72,
                                  String name73, String type73, Object value73,
                                  String name74, String type74, Object value74,
                                  String name75, String type75, Object value75,
                                  String name76, String type76, Object value76,
                                  String name77, String type77, Object value77,
                                  String name78, String type78, Object value78,
                                  String name79, String type79, Object value79 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
    _symbols.add( createSymbol( name70, typeFromValue( type70, value70 ), value70 ) );
    _symbols.add( createSymbol( name71, typeFromValue( type71, value71 ), value71 ) );
    _symbols.add( createSymbol( name72, typeFromValue( type72, value72 ), value72 ) );
    _symbols.add( createSymbol( name73, typeFromValue( type73, value73 ), value73 ) );
    _symbols.add( createSymbol( name74, typeFromValue( type74, value74 ), value74 ) );
    _symbols.add( createSymbol( name75, typeFromValue( type75, value75 ), value75 ) );
    _symbols.add( createSymbol( name76, typeFromValue( type76, value76 ), value76 ) );
    _symbols.add( createSymbol( name77, typeFromValue( type77, value77 ), value77 ) );
    _symbols.add( createSymbol( name78, typeFromValue( type78, value78 ), value78 ) );
    _symbols.add( createSymbol( name79, typeFromValue( type79, value79 ), value79 ) );
  }

  public void addLocalSymbols_80( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69,
                                  String name70, String type70, Object value70,
                                  String name71, String type71, Object value71,
                                  String name72, String type72, Object value72,
                                  String name73, String type73, Object value73,
                                  String name74, String type74, Object value74,
                                  String name75, String type75, Object value75,
                                  String name76, String type76, Object value76,
                                  String name77, String type77, Object value77,
                                  String name78, String type78, Object value78,
                                  String name79, String type79, Object value79,
                                  String name80, String type80, Object value80 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
    _symbols.add( createSymbol( name70, typeFromValue( type70, value70 ), value70 ) );
    _symbols.add( createSymbol( name71, typeFromValue( type71, value71 ), value71 ) );
    _symbols.add( createSymbol( name72, typeFromValue( type72, value72 ), value72 ) );
    _symbols.add( createSymbol( name73, typeFromValue( type73, value73 ), value73 ) );
    _symbols.add( createSymbol( name74, typeFromValue( type74, value74 ), value74 ) );
    _symbols.add( createSymbol( name75, typeFromValue( type75, value75 ), value75 ) );
    _symbols.add( createSymbol( name76, typeFromValue( type76, value76 ), value76 ) );
    _symbols.add( createSymbol( name77, typeFromValue( type77, value77 ), value77 ) );
    _symbols.add( createSymbol( name78, typeFromValue( type78, value78 ), value78 ) );
    _symbols.add( createSymbol( name79, typeFromValue( type79, value79 ), value79 ) );
    _symbols.add( createSymbol( name80, typeFromValue( type80, value80 ), value80 ) );
  }

  public void addLocalSymbols_81( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69,
                                  String name70, String type70, Object value70,
                                  String name71, String type71, Object value71,
                                  String name72, String type72, Object value72,
                                  String name73, String type73, Object value73,
                                  String name74, String type74, Object value74,
                                  String name75, String type75, Object value75,
                                  String name76, String type76, Object value76,
                                  String name77, String type77, Object value77,
                                  String name78, String type78, Object value78,
                                  String name79, String type79, Object value79,
                                  String name80, String type80, Object value80,
                                  String name81, String type81, Object value81 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
    _symbols.add( createSymbol( name70, typeFromValue( type70, value70 ), value70 ) );
    _symbols.add( createSymbol( name71, typeFromValue( type71, value71 ), value71 ) );
    _symbols.add( createSymbol( name72, typeFromValue( type72, value72 ), value72 ) );
    _symbols.add( createSymbol( name73, typeFromValue( type73, value73 ), value73 ) );
    _symbols.add( createSymbol( name74, typeFromValue( type74, value74 ), value74 ) );
    _symbols.add( createSymbol( name75, typeFromValue( type75, value75 ), value75 ) );
    _symbols.add( createSymbol( name76, typeFromValue( type76, value76 ), value76 ) );
    _symbols.add( createSymbol( name77, typeFromValue( type77, value77 ), value77 ) );
    _symbols.add( createSymbol( name78, typeFromValue( type78, value78 ), value78 ) );
    _symbols.add( createSymbol( name79, typeFromValue( type79, value79 ), value79 ) );
    _symbols.add( createSymbol( name80, typeFromValue( type80, value80 ), value80 ) );
    _symbols.add( createSymbol( name81, typeFromValue( type81, value81 ), value81 ) );
  }

  public void addLocalSymbols_82( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69,
                                  String name70, String type70, Object value70,
                                  String name71, String type71, Object value71,
                                  String name72, String type72, Object value72,
                                  String name73, String type73, Object value73,
                                  String name74, String type74, Object value74,
                                  String name75, String type75, Object value75,
                                  String name76, String type76, Object value76,
                                  String name77, String type77, Object value77,
                                  String name78, String type78, Object value78,
                                  String name79, String type79, Object value79,
                                  String name80, String type80, Object value80,
                                  String name81, String type81, Object value81,
                                  String name82, String type82, Object value82 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
    _symbols.add( createSymbol( name70, typeFromValue( type70, value70 ), value70 ) );
    _symbols.add( createSymbol( name71, typeFromValue( type71, value71 ), value71 ) );
    _symbols.add( createSymbol( name72, typeFromValue( type72, value72 ), value72 ) );
    _symbols.add( createSymbol( name73, typeFromValue( type73, value73 ), value73 ) );
    _symbols.add( createSymbol( name74, typeFromValue( type74, value74 ), value74 ) );
    _symbols.add( createSymbol( name75, typeFromValue( type75, value75 ), value75 ) );
    _symbols.add( createSymbol( name76, typeFromValue( type76, value76 ), value76 ) );
    _symbols.add( createSymbol( name77, typeFromValue( type77, value77 ), value77 ) );
    _symbols.add( createSymbol( name78, typeFromValue( type78, value78 ), value78 ) );
    _symbols.add( createSymbol( name79, typeFromValue( type79, value79 ), value79 ) );
    _symbols.add( createSymbol( name80, typeFromValue( type80, value80 ), value80 ) );
    _symbols.add( createSymbol( name81, typeFromValue( type81, value81 ), value81 ) );
    _symbols.add( createSymbol( name82, typeFromValue( type82, value82 ), value82 ) );
  }

  public void addLocalSymbols_83( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69,
                                  String name70, String type70, Object value70,
                                  String name71, String type71, Object value71,
                                  String name72, String type72, Object value72,
                                  String name73, String type73, Object value73,
                                  String name74, String type74, Object value74,
                                  String name75, String type75, Object value75,
                                  String name76, String type76, Object value76,
                                  String name77, String type77, Object value77,
                                  String name78, String type78, Object value78,
                                  String name79, String type79, Object value79,
                                  String name80, String type80, Object value80,
                                  String name81, String type81, Object value81,
                                  String name82, String type82, Object value82,
                                  String name83, String type83, Object value83 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
    _symbols.add( createSymbol( name70, typeFromValue( type70, value70 ), value70 ) );
    _symbols.add( createSymbol( name71, typeFromValue( type71, value71 ), value71 ) );
    _symbols.add( createSymbol( name72, typeFromValue( type72, value72 ), value72 ) );
    _symbols.add( createSymbol( name73, typeFromValue( type73, value73 ), value73 ) );
    _symbols.add( createSymbol( name74, typeFromValue( type74, value74 ), value74 ) );
    _symbols.add( createSymbol( name75, typeFromValue( type75, value75 ), value75 ) );
    _symbols.add( createSymbol( name76, typeFromValue( type76, value76 ), value76 ) );
    _symbols.add( createSymbol( name77, typeFromValue( type77, value77 ), value77 ) );
    _symbols.add( createSymbol( name78, typeFromValue( type78, value78 ), value78 ) );
    _symbols.add( createSymbol( name79, typeFromValue( type79, value79 ), value79 ) );
    _symbols.add( createSymbol( name80, typeFromValue( type80, value80 ), value80 ) );
    _symbols.add( createSymbol( name81, typeFromValue( type81, value81 ), value81 ) );
    _symbols.add( createSymbol( name82, typeFromValue( type82, value82 ), value82 ) );
    _symbols.add( createSymbol( name83, typeFromValue( type83, value83 ), value83 ) );
  }

  public void addLocalSymbols_84( String name1, String type1, Object value1,
                                  String name2, String type2, Object value2,
                                  String name3, String type3, Object value3,
                                  String name4, String type4, Object value4,
                                  String name5, String type5, Object value5,
                                  String name6, String type6, Object value6,
                                  String name7, String type7, Object value7,
                                  String name8, String type8, Object value8,
                                  String name9, String type9, Object value9,
                                  String name10, String type10, Object value10,
                                  String name11, String type11, Object value11,
                                  String name12, String type12, Object value12,
                                  String name13, String type13, Object value13,
                                  String name14, String type14, Object value14,
                                  String name15, String type15, Object value15,
                                  String name16, String type16, Object value16,
                                  String name17, String type17, Object value17,
                                  String name18, String type18, Object value18,
                                  String name19, String type19, Object value19,
                                  String name20, String type20, Object value20,
                                  String name21, String type21, Object value21,
                                  String name22, String type22, Object value22,
                                  String name23, String type23, Object value23,
                                  String name24, String type24, Object value24,
                                  String name25, String type25, Object value25,
                                  String name26, String type26, Object value26,
                                  String name27, String type27, Object value27,
                                  String name28, String type28, Object value28,
                                  String name29, String type29, Object value29,
                                  String name30, String type30, Object value30,
                                  String name31, String type31, Object value31,
                                  String name32, String type32, Object value32,
                                  String name33, String type33, Object value33,
                                  String name34, String type34, Object value34,
                                  String name35, String type35, Object value35,
                                  String name36, String type36, Object value36,
                                  String name37, String type37, Object value37,
                                  String name38, String type38, Object value38,
                                  String name39, String type39, Object value39,
                                  String name40, String type40, Object value40,
                                  String name41, String type41, Object value41,
                                  String name42, String type42, Object value42,
                                  String name43, String type43, Object value43,
                                  String name44, String type44, Object value44,
                                  String name45, String type45, Object value45,
                                  String name46, String type46, Object value46,
                                  String name47, String type47, Object value47,
                                  String name48, String type48, Object value48,
                                  String name49, String type49, Object value49,
                                  String name50, String type50, Object value50,
                                  String name51, String type51, Object value51,
                                  String name52, String type52, Object value52,
                                  String name53, String type53, Object value53,
                                  String name54, String type54, Object value54,
                                  String name55, String type55, Object value55,
                                  String name56, String type56, Object value56,
                                  String name57, String type57, Object value57,
                                  String name58, String type58, Object value58,
                                  String name59, String type59, Object value59,
                                  String name60, String type60, Object value60,
                                  String name61, String type61, Object value61,
                                  String name62, String type62, Object value62,
                                  String name63, String type63, Object value63,
                                  String name64, String type64, Object value64,
                                  String name65, String type65, Object value65,
                                  String name66, String type66, Object value66,
                                  String name67, String type67, Object value67,
                                  String name68, String type68, Object value68,
                                  String name69, String type69, Object value69,
                                  String name70, String type70, Object value70,
                                  String name71, String type71, Object value71,
                                  String name72, String type72, Object value72,
                                  String name73, String type73, Object value73,
                                  String name74, String type74, Object value74,
                                  String name75, String type75, Object value75,
                                  String name76, String type76, Object value76,
                                  String name77, String type77, Object value77,
                                  String name78, String type78, Object value78,
                                  String name79, String type79, Object value79,
                                  String name80, String type80, Object value80,
                                  String name81, String type81, Object value81,
                                  String name82, String type82, Object value82,
                                  String name83, String type83, Object value83,
                                  String name84, String type84, Object value84 )
  {
    _symbols.add( createSymbol( name1, typeFromValue( type1, value1 ), value1 ) );
    _symbols.add( createSymbol( name2, typeFromValue( type2, value2 ), value2 ) );
    _symbols.add( createSymbol( name3, typeFromValue( type3, value3 ), value3 ) );
    _symbols.add( createSymbol( name4, typeFromValue( type4, value4 ), value4 ) );
    _symbols.add( createSymbol( name5, typeFromValue( type5, value5 ), value5 ) );
    _symbols.add( createSymbol( name6, typeFromValue( type6, value6 ), value6 ) );
    _symbols.add( createSymbol( name7, typeFromValue( type7, value7 ), value7 ) );
    _symbols.add( createSymbol( name8, typeFromValue( type8, value8 ), value8 ) );
    _symbols.add( createSymbol( name9, typeFromValue( type9, value9 ), value9 ) );
    _symbols.add( createSymbol( name10, typeFromValue( type10, value10 ), value10 ) );
    _symbols.add( createSymbol( name11, typeFromValue( type11, value11 ), value11 ) );
    _symbols.add( createSymbol( name12, typeFromValue( type12, value12 ), value12 ) );
    _symbols.add( createSymbol( name13, typeFromValue( type13, value13 ), value13 ) );
    _symbols.add( createSymbol( name14, typeFromValue( type14, value14 ), value14 ) );
    _symbols.add( createSymbol( name15, typeFromValue( type15, value15 ), value15 ) );
    _symbols.add( createSymbol( name16, typeFromValue( type16, value16 ), value16 ) );
    _symbols.add( createSymbol( name17, typeFromValue( type17, value17 ), value17 ) );
    _symbols.add( createSymbol( name18, typeFromValue( type18, value18 ), value18 ) );
    _symbols.add( createSymbol( name19, typeFromValue( type19, value19 ), value19 ) );
    _symbols.add( createSymbol( name20, typeFromValue( type20, value20 ), value20 ) );
    _symbols.add( createSymbol( name21, typeFromValue( type21, value21 ), value21 ) );
    _symbols.add( createSymbol( name22, typeFromValue( type22, value22 ), value22 ) );
    _symbols.add( createSymbol( name23, typeFromValue( type23, value23 ), value23 ) );
    _symbols.add( createSymbol( name24, typeFromValue( type24, value24 ), value24 ) );
    _symbols.add( createSymbol( name25, typeFromValue( type25, value25 ), value25 ) );
    _symbols.add( createSymbol( name26, typeFromValue( type26, value26 ), value26 ) );
    _symbols.add( createSymbol( name27, typeFromValue( type27, value27 ), value27 ) );
    _symbols.add( createSymbol( name28, typeFromValue( type28, value28 ), value28 ) );
    _symbols.add( createSymbol( name29, typeFromValue( type29, value29 ), value29 ) );
    _symbols.add( createSymbol( name30, typeFromValue( type30, value30 ), value30 ) );
    _symbols.add( createSymbol( name31, typeFromValue( type31, value31 ), value31 ) );
    _symbols.add( createSymbol( name32, typeFromValue( type32, value32 ), value32 ) );
    _symbols.add( createSymbol( name33, typeFromValue( type33, value33 ), value33 ) );
    _symbols.add( createSymbol( name34, typeFromValue( type34, value34 ), value34 ) );
    _symbols.add( createSymbol( name35, typeFromValue( type35, value35 ), value35 ) );
    _symbols.add( createSymbol( name36, typeFromValue( type36, value36 ), value36 ) );
    _symbols.add( createSymbol( name37, typeFromValue( type37, value37 ), value37 ) );
    _symbols.add( createSymbol( name38, typeFromValue( type38, value38 ), value38 ) );
    _symbols.add( createSymbol( name39, typeFromValue( type39, value39 ), value39 ) );
    _symbols.add( createSymbol( name40, typeFromValue( type40, value40 ), value40 ) );
    _symbols.add( createSymbol( name41, typeFromValue( type41, value41 ), value41 ) );
    _symbols.add( createSymbol( name42, typeFromValue( type42, value42 ), value42 ) );
    _symbols.add( createSymbol( name43, typeFromValue( type43, value43 ), value43 ) );
    _symbols.add( createSymbol( name44, typeFromValue( type44, value44 ), value44 ) );
    _symbols.add( createSymbol( name45, typeFromValue( type45, value45 ), value45 ) );
    _symbols.add( createSymbol( name46, typeFromValue( type46, value46 ), value46 ) );
    _symbols.add( createSymbol( name47, typeFromValue( type47, value47 ), value47 ) );
    _symbols.add( createSymbol( name48, typeFromValue( type48, value48 ), value48 ) );
    _symbols.add( createSymbol( name49, typeFromValue( type49, value49 ), value49 ) );
    _symbols.add( createSymbol( name50, typeFromValue( type50, value50 ), value50 ) );
    _symbols.add( createSymbol( name51, typeFromValue( type51, value51 ), value51 ) );
    _symbols.add( createSymbol( name52, typeFromValue( type52, value52 ), value52 ) );
    _symbols.add( createSymbol( name53, typeFromValue( type53, value53 ), value53 ) );
    _symbols.add( createSymbol( name54, typeFromValue( type54, value54 ), value54 ) );
    _symbols.add( createSymbol( name55, typeFromValue( type55, value55 ), value55 ) );
    _symbols.add( createSymbol( name56, typeFromValue( type56, value56 ), value56 ) );
    _symbols.add( createSymbol( name57, typeFromValue( type57, value57 ), value57 ) );
    _symbols.add( createSymbol( name58, typeFromValue( type58, value58 ), value58 ) );
    _symbols.add( createSymbol( name59, typeFromValue( type59, value59 ), value59 ) );
    _symbols.add( createSymbol( name60, typeFromValue( type60, value60 ), value60 ) );
    _symbols.add( createSymbol( name61, typeFromValue( type61, value61 ), value61 ) );
    _symbols.add( createSymbol( name62, typeFromValue( type62, value62 ), value62 ) );
    _symbols.add( createSymbol( name63, typeFromValue( type63, value63 ), value63 ) );
    _symbols.add( createSymbol( name64, typeFromValue( type64, value64 ), value64 ) );
    _symbols.add( createSymbol( name65, typeFromValue( type65, value65 ), value65 ) );
    _symbols.add( createSymbol( name66, typeFromValue( type66, value66 ), value66 ) );
    _symbols.add( createSymbol( name67, typeFromValue( type67, value67 ), value67 ) );
    _symbols.add( createSymbol( name68, typeFromValue( type68, value68 ), value68 ) );
    _symbols.add( createSymbol( name69, typeFromValue( type69, value69 ), value69 ) );
    _symbols.add( createSymbol( name70, typeFromValue( type70, value70 ), value70 ) );
    _symbols.add( createSymbol( name71, typeFromValue( type71, value71 ), value71 ) );
    _symbols.add( createSymbol( name72, typeFromValue( type72, value72 ), value72 ) );
    _symbols.add( createSymbol( name73, typeFromValue( type73, value73 ), value73 ) );
    _symbols.add( createSymbol( name74, typeFromValue( type74, value74 ), value74 ) );
    _symbols.add( createSymbol( name75, typeFromValue( type75, value75 ), value75 ) );
    _symbols.add( createSymbol( name76, typeFromValue( type76, value76 ), value76 ) );
    _symbols.add( createSymbol( name77, typeFromValue( type77, value77 ), value77 ) );
    _symbols.add( createSymbol( name78, typeFromValue( type78, value78 ), value78 ) );
    _symbols.add( createSymbol( name79, typeFromValue( type79, value79 ), value79 ) );
    _symbols.add( createSymbol( name80, typeFromValue( type80, value80 ), value80 ) );
    _symbols.add( createSymbol( name81, typeFromValue( type81, value81 ), value81 ) );
    _symbols.add( createSymbol( name82, typeFromValue( type82, value82 ), value82 ) );
    _symbols.add( createSymbol( name83, typeFromValue( type83, value83 ), value83 ) );
    _symbols.add( createSymbol( name84, typeFromValue( type84, value84 ), value84 ) );
  }
}
