package gw.lang.parser.coercers;

import gw.config.CommonServices;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class StringCoercer extends StandardCoercer
{
  private static final StringCoercer INSTANCE = new StringCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeStringFrom( value );
  }

  public static StringCoercer instance()
  {
    return INSTANCE;
  }
}
