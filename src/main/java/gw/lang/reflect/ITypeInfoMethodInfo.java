package gw.lang.reflect;

/**
 * Indicates that the methods represented by this method info, which should be static,
 * require evaluation of the root expression because they are derived from the
 * type itself, rather than from static information available at the call site.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITypeInfoMethodInfo extends IMethodInfo
{
  IMethodInfo getBackingMethodInfo();
}