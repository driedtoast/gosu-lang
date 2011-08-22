package gw.lang.parser.coercers;

import gw.lang.reflect.java.IJavaType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class CharPCoercer extends BasePrimitiveCoercer
{
  private static final CharPCoercer INSTANCE = new CharPCoercer();

  public CharPCoercer()
  {
    super( CharCoercer.instance(), IJavaType.pCHAR, IJavaType.CHARACTER );
  }

  public static CharPCoercer instance()
  {
    return INSTANCE;
  }
}