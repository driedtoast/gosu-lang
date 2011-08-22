package gw.lang.reflect.java;

import gw.lang.javadoc.JavaHasParams;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IParameterInfo;

import java.lang.reflect.Constructor;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaConstructorInfo extends IAttributedFeatureInfo, IConstructorInfo, JavaHasParams
{
  IParameterInfo[] getGenericParameters();

  Constructor getJavaConstructor();
}
