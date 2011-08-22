package gw.lang.reflect.java;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaClassParameterizedType extends IJavaClassType {
  IJavaClassType[] getActualTypeArguments();

  IJavaClassType getRawType();
}