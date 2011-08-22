package gw.lang.reflect.java;

import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;

import java.io.Serializable;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaPropertyDescriptor extends Serializable {
  String getName();

  IJavaClassMethod getReadMethod();
  IJavaClassMethod getWriteMethod();

  IType getPropertyType();

  IJavaClassInfo getPropertyClassInfo();

  boolean isHidden();

  boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint);

  boolean isHiddenViaFeatureDescriptor();

  boolean isDeprecated();

  String getDisplayName();

  String getShortDescription();
}