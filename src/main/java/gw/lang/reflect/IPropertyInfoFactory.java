package gw.lang.reflect;

import gw.lang.UnstableAPI;

import java.beans.IntrospectionException;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IPropertyInfoFactory
{
  /**
   * Make a property on the specified java class.
   *
   * @param container Typically the ITypeInfo instance exposing this property
   * @param strName   The name for the property.
   * @param javaClass The java class having the method[s] you want to expose
   *                  as a property.
   * @param strGetter The name of the getter method defined in the java class.
   * @param strSetter Optional setter method name.
   *
   * @return A property for the java class.
   */
  public IPropertyInfo make( IFeatureInfo container, String strName, Class javaClass, String strGetter, String strSetter ) throws IntrospectionException;

  public IPropertyInfo make( IFeatureInfo container, String strName, Class javaClass, String strGetter, String strSetter, IType propertyType ) throws IntrospectionException;

  public IPropertyInfo make( IFeatureInfo container, String strName, Class javaClass, String strGetter, String strSetter, IType propertyType, IPresentationInfo presInfo ) throws IntrospectionException;
}
