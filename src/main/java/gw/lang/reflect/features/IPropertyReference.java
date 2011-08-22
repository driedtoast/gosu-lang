package gw.lang.reflect.features;

import gw.lang.reflect.IPropertyInfo;

public interface IPropertyReference<R, T> extends IFeatureReference<R, T>
{
  public IPropertyInfo getPropertyInfo();
}
