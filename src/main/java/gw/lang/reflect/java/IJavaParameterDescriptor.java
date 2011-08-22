package gw.lang.reflect.java;

import gw.lang.reflect.IType;

import java.io.Serializable;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaParameterDescriptor extends Serializable {
  String getName();

  String getDisplayName();

  String getShortDescription();

  boolean isHidden();

  IType getFeatureType();
}