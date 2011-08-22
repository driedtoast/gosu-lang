package gw.lang.parser.coercers;

import gw.lang.reflect.java.IJavaType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class IntPCoercer extends BasePrimitiveCoercer
{
  private static final IntPCoercer INSTANCE = new IntPCoercer();

  public IntPCoercer()
  {
    super( IntCoercer.instance(), IJavaType.pINT, IJavaType.INTEGER );
  }

  public static IntPCoercer instance()
  {
    return INSTANCE;
  }
}