package gw.lang.reflect.java;


import gw.lang.javadoc.IDocRef;
import gw.lang.javadoc.IMethodNode;
import gw.lang.javadoc.JavaHasParams;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IGenericMethodInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;

import java.lang.reflect.Method;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaMethodInfo extends IAttributedFeatureInfo, IMethodInfo, IGenericMethodInfo, JavaHasParams
{
  IParameterInfo[] getGenericParameters();

  IType getGenericReturnType();

  String getShortDescription();

  IJavaClassMethod getMethod();

  IDocRef<IMethodNode> getMethodDocs();

  Method getRawMethod();

  int getModifiers();
}
