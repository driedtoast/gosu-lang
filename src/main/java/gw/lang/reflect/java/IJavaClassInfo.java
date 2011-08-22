package gw.lang.reflect.java;

import gw.lang.javadoc.IClassDocNode;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.module.IModule;

import java.lang.reflect.AnnotatedElement;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaClassInfo extends AnnotatedElement, IJavaClassType {
  boolean isAnnotation();

  boolean isInterface();

  String getName();

  IJavaClassMethod getMethod(String methodName, IJavaClassInfo... paramTypes) throws NoSuchMethodException;

  IJavaClassMethod[] getDeclaredMethods();

  Object newInstance() throws InstantiationException, IllegalAccessException;

  Object[] getEnumConstants();

  IType getJavaType();

  IJavaClassInfo[] getInterfaces();

  IJavaClassInfo getSuperclass();

  String[] getTypeParameterNames();

  IJavaTypeVariable[] getTypeParameters();
  
  boolean isTypeGosuClassInstance();

  IJavaClassField[] getDeclaredFields();

  IJavaClassConstructor[] getDeclaredConstructors();

  IClassDocNode createClassDocNode();

  IJavaPropertyDescriptor[] getPropertyDescriptors();

  IJavaMethodDescriptor[] getMethodDescriptors();

  boolean hasCustomBeanInfo();

  String getRelativeName();

  String getDisplayName();

  String getShortDescription();

  boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint);

  boolean isHiddenViaFeatureDescriptor();

  IJavaClassField[] getFields();

  boolean isArray();

  IJavaClassInfo getComponentType();

  boolean isEnum();

  IModule getModule();

  int getModifiers();

  boolean isPrimitive();

  IType getEnclosingType();

  String getNamespace();

  IJavaClassType[] getGenericInterfaces();

  IJavaClassType getGenericSuperclass();

  IType[] getInnerClasses();

  IJavaClassInfo getArrayType();

  IJavaClassInfo[] getDeclaredClasses();

  boolean isAnonymousClass();

  boolean isAssignableFrom(IJavaClassInfo aClass);
}