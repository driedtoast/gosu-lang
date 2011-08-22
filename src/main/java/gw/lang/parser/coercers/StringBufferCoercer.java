package gw.lang.parser.coercers;

import gw.config.CommonServices;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class StringBufferCoercer extends StandardCoercer
{
  private static final StringBufferCoercer INSTANCE = new StringBufferCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return new StringBuffer( CommonServices.getCoercionManager().makeStringFrom( value ) );
  }

  public static StringBufferCoercer instance()
  {
    return INSTANCE;
  }
}