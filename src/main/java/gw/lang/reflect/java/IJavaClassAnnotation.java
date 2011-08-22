package gw.lang.reflect.java;

import java.io.Serializable;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaClassAnnotation extends Serializable {
  String annotationTypeName();

  Object getValue(String name);
}