package gw.lang.parser.coercers;

import gw.lang.reflect.java.IJavaType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class FloatPCoercer extends BasePrimitiveCoercer
{
  private static final FloatPCoercer INSTANCE = new FloatPCoercer();

  public FloatPCoercer()
  {
    super( FloatCoercer.instance(), IJavaType.pFLOAT, IJavaType.FLOAT );
  }

  public static FloatPCoercer instance()
  {
    return INSTANCE;
  }
}