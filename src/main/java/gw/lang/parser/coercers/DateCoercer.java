package gw.lang.parser.coercers;

import gw.config.CommonServices;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class DateCoercer extends StandardCoercer
{
  private static final DateCoercer INSTANCE = new DateCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeDateFrom( value );
  }

  public static DateCoercer instance()
  {
    return INSTANCE;
  }
}