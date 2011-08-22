package gw.lang.reflect.gs;

import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IGenericMethodInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IOptionalParamCapable;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuConstructorInfo extends IAttributedFeatureInfo, IGenericMethodInfo, IConstructorInfo, IOptionalParamCapable
{  
  public IDynamicFunctionSymbol getDfs();
}
