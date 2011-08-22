package gw.lang.debugger;

import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.reflect.gs.IGosuClass;
import java.io.Serializable;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class GosuClassDebugContextProperties implements IDebugContextProperties
{
  public static final String CTX_ID = "_strName";
  public static final String CTX_MODULE = "_strModule";

  private CaseInsensitiveCharSequence _id;
  private String _module;

  public GosuClassDebugContextProperties( String strId, String module )
  {
    _id = CaseInsensitiveCharSequence.get( strId );
    _module = module;
  }

  public Serializable getContextProperty( String strProperty )
  {
    if( strProperty.equals( CTX_ID ) )
    {
      return _id;
    }
    if( strProperty.equals( CTX_MODULE ) )
    {
      return _module;
    }

    throw new RuntimeException( "Invalid context property: " + strProperty );
  }

  public String getFormattedStackTraceTitle( String strContext, DebugLocationContext location )
  {
    return strContext + location.getTitle();
  }

  public String getBreakpointTitle()
  {
    return _id.toString();
  }

  public String getEnclosingType()
  {
    return _id.toString();
  }

  public boolean hasSource()
  {
    return getContextProperty( GosuClassDebugContextProperties.CTX_ID ) != null;
  }

  public boolean isDebuggable()
  {
    return !IGosuClass.ProxyUtil.isProxyStart( _id.toString() ) &&
           !_id.toString().startsWith( "pcf." ); //## todo: make this pluggable
  }

  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( !(o instanceof GosuClassDebugContextProperties) )
    {
      return false;
    }

    final GosuClassDebugContextProperties gosuClassDebugContextProperties = (GosuClassDebugContextProperties)o;

    return _id.equals(gosuClassDebugContextProperties._id);
  }

  public int hashCode()
  {
    int result;
    result = _id.hashCode();
    return result;
  }

  public String toString()
  {
    return "Class Name: " + _id;
  }
}
