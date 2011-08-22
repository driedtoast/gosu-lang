package gw.lang.reflect.gs;

import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.ITypeInfo;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuClassTypeInfo extends IAttributedFeatureInfo, ITypeInfo, IRelativeTypeInfo
{
  IGosuClass getGosuClass();
}
