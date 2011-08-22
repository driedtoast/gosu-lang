package gw.lang.parser.coercers;

import gw.lang.reflect.java.IJavaType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class DoublePCoercer extends BasePrimitiveCoercer
{
  private static final DoublePCoercer INSTANCE = new DoublePCoercer();

  public DoublePCoercer()
  {
    super( DoubleCoercer.instance(), IJavaType.pDOUBLE, IJavaType.DOUBLE );
  }

  public static DoublePCoercer instance()
  {
    return INSTANCE;
  }
}