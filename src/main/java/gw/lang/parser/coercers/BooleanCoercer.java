package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.config.CommonServices;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class BooleanCoercer extends BaseBoxedCoercer
{
  private static final BooleanCoercer INSTANCE = new BooleanCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeBooleanFrom( value );
  }

  public static BooleanCoercer instance()
  {
    return INSTANCE;
  }
}
