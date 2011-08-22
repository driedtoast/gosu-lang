package gw.lang.reflect;

/**
 * Works in conjunction with IMethodInfo.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IMethodCallHandler
{
  public Object handleCall( Object ctx, Object... args );
}
