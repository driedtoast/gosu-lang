package gw.lang.reflect;

/**
 * Works in conjunction with IConstructorInfo.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IConstructorHandler
{
  public Object newInstance( Object... args );
}
