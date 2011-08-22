package gw.lang.debugger;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.INonLoadableType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

import java.io.Serializable;
import java.util.Collections;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class DebugExpression implements Serializable
{
  private String _strExpression;
  private String _strType;
  private String _strValue;
  private boolean _bLoadableType;

  public DebugExpression( String strExpression, IType type, String strValue )
  {
    _strExpression = strExpression;
    assignTypeName(type);
    _strValue = strValue;
  }

  public String getExpression()
  {
    return _strExpression;
  }

  public IType getType()
  {
    return TypeSystem.parseType( _strType, TypeVarToTypeMap.EMPTY_MAP );
  }

  public void setType( IType type )
  {
    assignTypeName( type );
  }

  public String getTypeName()
  {
    return _strType;
  }

  public String getValue()
  {
    return _strValue;
  }

  public boolean isLoadableType() {
    return _bLoadableType;
  }

  private void assignTypeName(IType type)
  {
    _strType = type.getName();
    _bLoadableType = !(type instanceof INonLoadableType);
  }
}
