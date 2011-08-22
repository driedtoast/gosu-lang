package gw.lang.parser.coercers;

import gw.lang.parser.ICoercer;
import gw.lang.reflect.IType;
import gw.config.CommonServices;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class TypeVariableCoercer implements ICoercer
{
  private static final TypeVariableCoercer INSTANCE = new TypeVariableCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().convertValue( value, typeToCoerceTo );
  }

  public boolean isExplicitCoercion()
  {
    return false;
  }

  public boolean handlesNull()
  {
    return true;
  }

  public int getPriority( IType to, IType from )
  {
    return 0;
  }

  public static TypeVariableCoercer instance()
  {
    return INSTANCE;
  }
}
