package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.config.CommonServices;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class LongCoercer extends BaseBoxedCoercer
{
  private static final LongCoercer INSTANCE = new LongCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeLongFrom( value );
  }

  public static LongCoercer instance()
  {
    return INSTANCE;
  }
}