package gw.util;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IFeatureFilter
{
  public boolean acceptFeature(IType beanType, IFeatureInfo fi);
}
