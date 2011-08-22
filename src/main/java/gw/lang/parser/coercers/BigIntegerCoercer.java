package gw.lang.parser.coercers;

import gw.config.CommonServices;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class BigIntegerCoercer extends StandardCoercer
{
  private static final BigIntegerCoercer INSTANCE = new BigIntegerCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeBigIntegerFrom( value );
  }

  public static BigIntegerCoercer instance()
  {
    return INSTANCE;
  }
}