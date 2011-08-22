package gw.lang.reflect;

import java.lang.reflect.Method;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IMethodInfoFactory
{
  public IMethodInfo make(IFeatureInfo container, Method method);

  public IMethodInfo make(IFeatureInfo container, Method method, String description);
}
