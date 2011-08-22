package gw.lang.parser.coercers;

import gw.config.CommonServices;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ByteCoercer extends BaseBoxedCoercer
{
  private static final ByteCoercer INSTANCE = new ByteCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    Integer integer = CommonServices.getCoercionManager().makeIntegerFrom(value );
    return integer.byteValue();
  }

  public static ByteCoercer instance()
  {
    return INSTANCE;
  }
}