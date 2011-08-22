package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.config.CommonServices;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class DoubleCoercer extends BaseBoxedCoercer
{
  private static final DoubleCoercer INSTANCE = new DoubleCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeDoubleFrom( value );
  }

  public static DoubleCoercer instance()
  {
    return INSTANCE;
  }
}