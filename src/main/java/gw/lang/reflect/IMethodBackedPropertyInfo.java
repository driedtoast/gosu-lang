package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IMethodBackedPropertyInfo {

  IMethodInfo getReadMethodInfo();

  IMethodInfo getWriteMethodInfo();

}
