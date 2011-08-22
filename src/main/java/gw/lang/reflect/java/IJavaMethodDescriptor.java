package gw.lang.reflect.java;

import gw.lang.reflect.IScriptabilityModifier;

import java.io.Serializable;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaMethodDescriptor extends Serializable {
  IJavaClassMethod getMethod();

  String getName();

  IJavaParameterDescriptor[] getParameterDescriptors();

  boolean isHiddenViaFeatureDescriptor();

  boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint);
}