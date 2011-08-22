package gw.lang.reflect.java;

import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IPropertyInfo;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaFieldPropertyInfo extends IAttributedFeatureInfo, IPropertyInfo, IJavaBasePropertyInfo
{
  IJavaClassField getField();
}
