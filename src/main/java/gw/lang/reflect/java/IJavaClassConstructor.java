package gw.lang.reflect.java;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IFeatureInfo;

import java.lang.reflect.AnnotatedElement;
import java.io.Serializable;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaClassConstructor extends AnnotatedElement, Serializable {
  IJavaClassInfo[] getExceptionTypes();

  int getModifiers();

  boolean isSynthetic();

  IParameterInfo[] convertGenericParameterTypes(IFeatureInfo container, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars);

  IJavaClassInfo[] getParameterTypes();

  IJavaClassAnnotation[][] getParameterAnnotations();

  IJavaClassInfo getDeclaringClass();
}
