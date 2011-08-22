package gw.lang.parser.coercers;

import gw.lang.reflect.java.IJavaType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class LongPCoercer extends BasePrimitiveCoercer
{
  private static final LongPCoercer INSTANCE = new LongPCoercer();

  public LongPCoercer()
  {
    super( LongCoercer.instance(), IJavaType.pLONG, IJavaType.LONG );
  }

  public static LongPCoercer instance()
  {
    return INSTANCE;
  }
}