package gw.lang.reflect.java;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGenericTypeVariable;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.io.Serializable;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaClassMethod extends AnnotatedElement, Serializable {
  IType getReturnType();

  IJavaClassInfo getReturnClassInfo();

  String getName();

  IJavaClassInfo getDeclaringClass();

  IJavaClassInfo[] getParameterTypes();

  int getModifiers();

  boolean isSynthetic();

  boolean isBridge();

  IJavaClassInfo[] getExceptionTypes();

  Object getDefaultValue();

  String[] getParameterTypeDescriptors();

  String getReturnTypeDescriptor();

  boolean isReturnTypePrimitive();

  String getReturnTypeName();

  String[] getParameterTypeNames();

  boolean returnsNonVoid();

  Object invoke(Object ctx, Object[] args) throws InvocationTargetException, IllegalAccessException;

  IType getActualReturnType( TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars);

  IType[] getActualParameterTypes( TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars);

  IGenericTypeVariable[] getTypeVariables(IJavaMethodInfo javaMethodInfo);

  IJavaTypeVariable[] getTypeParameters();

  IJavaClassAnnotation[][] getParameterAnnotations();

  IJavaClassType[] getGenericParameterTypes();

  IJavaClassType getGenericReturnType();
}
