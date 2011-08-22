package gw.lang.reflect.java;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IType;

import java.lang.reflect.AnnotatedElement;
import java.io.Serializable;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaClassField extends AnnotatedElement, Serializable
{
  boolean isSynthetic();

  int getModifiers();

  IType getActualType( TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars);

  String getName();

  IType getDeclaringType();

  IType getType();

  IJavaClassInfo getTypeAsClassInfo();

  IJavaClassInfo getDeclaringClass();
}
