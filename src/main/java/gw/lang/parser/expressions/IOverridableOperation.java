package gw.lang.parser.expressions;

import gw.lang.reflect.IMethodInfo;

/**
 * Facilitates pseudo operator overloading on operations having one or more
 * IDimension operands.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IOverridableOperation
{
  IMethodInfo getOverride();
  void setOverride( IMethodInfo overrideMi );
}
