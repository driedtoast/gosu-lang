package gw.lang.parser.coercers;

import gw.lang.reflect.java.IJavaType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ShortPCoercer extends BasePrimitiveCoercer
{
  private static final ShortPCoercer INSTANCE = new ShortPCoercer();

  public ShortPCoercer()
  {
    super( ShortCoercer.instance(), IJavaType.pSHORT, IJavaType.SHORT );
  }

  public static ShortPCoercer instance()
  {
    return INSTANCE;
  }
}