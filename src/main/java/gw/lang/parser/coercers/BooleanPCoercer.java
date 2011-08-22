package gw.lang.parser.coercers;

import gw.lang.reflect.java.IJavaType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class BooleanPCoercer extends BasePrimitiveCoercer
{
  private static final BooleanPCoercer INSTANCE = new BooleanPCoercer();

  public BooleanPCoercer()
  {
    super( BooleanCoercer.instance(), IJavaType.pBOOLEAN, IJavaType.BOOLEAN );
  }

  public static BooleanPCoercer instance()
  {
    return INSTANCE;
  }
}