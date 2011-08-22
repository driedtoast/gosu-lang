package gw.lang.reflect.java;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaClassWildcardType extends IJavaClassType {
  IJavaClassType[] getUpperBounds();
}