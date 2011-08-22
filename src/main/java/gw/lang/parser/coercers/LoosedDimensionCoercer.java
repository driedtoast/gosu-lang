package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.TypeSystem;
import gw.config.CommonServices;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class LoosedDimensionCoercer extends StandardCoercer
{
  private static LoosedDimensionCoercer INSTANCE = new LoosedDimensionCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    IConstructorInfo ci = typeToCoerceTo.getTypeInfo().getCallableConstructor( TypeSystem.getFromObject( value ) );
    if( ci == null )
    {
      throw new RuntimeException( "Could not coerce " + value + " to " + typeToCoerceTo.getName() + ". No constructor found." );
    }
    return ci.getConstructor().newInstance( CommonServices.getCoercionManager().convertValue( value, ci.getParameters()[0].getFeatureType() ) );
  }

  public static LoosedDimensionCoercer instance()
  {
    return INSTANCE;
  }
}
