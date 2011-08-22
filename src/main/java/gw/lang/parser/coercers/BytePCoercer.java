package gw.lang.parser.coercers;

import gw.lang.reflect.java.IJavaType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class BytePCoercer extends BasePrimitiveCoercer
{
  private static final BytePCoercer INSTANCE = new BytePCoercer();

  public BytePCoercer()
  {
    super( ByteCoercer.instance(), IJavaType.pBYTE, IJavaType.BYTE );
  }

  public static BytePCoercer instance()
  {
    return INSTANCE;
  }
}