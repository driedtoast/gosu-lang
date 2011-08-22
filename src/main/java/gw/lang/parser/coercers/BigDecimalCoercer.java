package gw.lang.parser.coercers;

import gw.config.CommonServices;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class BigDecimalCoercer extends StandardCoercer
{
  private static final BigDecimalCoercer INSTANCE = new BigDecimalCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeBigDecimalFrom( value );
  }

  public static BigDecimalCoercer instance()
  {
    return INSTANCE;
  }
}