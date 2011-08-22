package gw.lang.reflect.gs;

import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IGenericMethodInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.reflect.IOptionalParamCapable;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuMethodInfo extends IAttributedFeatureInfo, IGenericMethodInfo, IMethodInfo, IOptionalParamCapable, Comparable
{
  CaseInsensitiveCharSequence getCaseInsensitiveName();

  boolean isMethodForProperty();

  IDynamicFunctionSymbol getDfs();
}
