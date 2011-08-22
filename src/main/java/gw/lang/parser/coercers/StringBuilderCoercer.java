package gw.lang.parser.coercers;

import gw.config.CommonServices;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class StringBuilderCoercer extends StandardCoercer
{
  private static final StringBuilderCoercer INSTANCE = new StringBuilderCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return new StringBuilder( CommonServices.getCoercionManager().makeStringFrom( value ) );
  }

  public static StringBuilderCoercer instance()
  {
    return INSTANCE;
  }
}