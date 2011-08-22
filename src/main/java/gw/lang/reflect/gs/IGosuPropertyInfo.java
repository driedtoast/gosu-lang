package gw.lang.reflect.gs;

import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IMethodBackedPropertyInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IGenericMethodInfo;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuPropertyInfo extends IAttributedFeatureInfo, IPropertyInfo, IGenericMethodInfo, IMethodBackedPropertyInfo
{
  String getShortDescription();

  IDynamicPropertySymbol getDps();
}
