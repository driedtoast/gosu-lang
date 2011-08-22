package gw.lang.reflect;

import gw.lang.reflect.java.IJavaTypeInfo;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITypeInfoFactory
{
  IJavaTypeInfo create(IType intrType, Class<?> backingClass);
}
