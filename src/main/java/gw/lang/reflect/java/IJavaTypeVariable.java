package gw.lang.reflect.java;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaTypeVariable {
  String getName();

  IJavaClassType[] getBounds();
}