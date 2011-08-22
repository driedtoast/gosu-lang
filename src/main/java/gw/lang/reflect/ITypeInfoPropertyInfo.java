package gw.lang.reflect;

/**
 * Indicates that the properties represented by this property info, which should be static,
 * require evaluation of the root expression because they are derived from the
 * type itself, rather than from static information available at the call site.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITypeInfoPropertyInfo extends IPropertyInfo
{
  IPropertyInfo getBackingPropertyInfo();
}
