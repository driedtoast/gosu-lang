package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.config.CommonServices;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ShortCoercer extends BaseBoxedCoercer
{
  private static final ShortCoercer INSTANCE = new ShortCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    Integer integer = CommonServices.getCoercionManager().makeIntegerFrom(value );
    return integer.shortValue();
  }

  public static ShortCoercer instance()
  {
    return INSTANCE;
  }
}