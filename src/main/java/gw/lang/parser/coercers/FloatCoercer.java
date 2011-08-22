package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.config.CommonServices;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class FloatCoercer extends BaseBoxedCoercer
{
  private static final FloatCoercer INSTANCE = new FloatCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getCoercionManager().makeFloatFrom( value );
  }

  public static FloatCoercer instance()
  {
    return INSTANCE;
  }
}