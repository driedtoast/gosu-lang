package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.config.CommonServices;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class PrimitiveCoercer extends BaseCoercer
{
  private static final PrimitiveCoercer INSTANCE = new PrimitiveCoercer();

  public static PrimitiveCoercer instance()
  {
    return INSTANCE;
  }

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    if( value == null )
    {
      return CommonServices.getCoercionManager().convertNullAsPrimitive( typeToCoerceTo, false );
    }
    else
    {
      return value;
    }
  }

  public boolean isExplicitCoercion()
  {
    return false;
  }

  public boolean handlesNull()
  {
    return true;
  }

  public int getPriority( IType to, IType from )
  {
    return 0;
  }
}