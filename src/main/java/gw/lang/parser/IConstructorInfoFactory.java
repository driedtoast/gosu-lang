package gw.lang.parser;

import gw.lang.javadoc.IConstructorNode;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IConstructorType;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.java.IJavaClassConstructor;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IConstructorInfoFactory
{
  IConstructorInfo create( IFeatureInfo container, IJavaClassConstructor ctor, IConstructorNode docs );

  IConstructorType makeConstructorType( IConstructorInfo ctor );
}
