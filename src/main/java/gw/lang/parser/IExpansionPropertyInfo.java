package gw.lang.parser;

import gw.lang.reflect.IPropertyInfo;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IExpansionPropertyInfo extends IPropertyInfo
{
  IPropertyInfo getDelegate();
}
