package gw.lang.javadoc;

import gw.lang.UnstableAPI;
import gw.lang.reflect.java.IJavaClassInfo;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IMethodNode extends IBaseFeatureNode {

  String getReturnDescription();

  void setReturnDescription( String value );

  List<IParamNode> getParams();

  void addParam( IParamNode param );

  List<IExceptionNode> getExceptions();

  void addException( IExceptionNode param );

  IExceptionNode getException( IJavaClassInfo exceptionClass );

  String getName();

  void setName( String name );
  
}
