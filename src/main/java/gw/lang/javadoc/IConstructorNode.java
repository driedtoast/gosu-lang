package gw.lang.javadoc;

import gw.lang.UnstableAPI;
import gw.lang.reflect.java.IJavaClassInfo;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IConstructorNode extends IBaseFeatureNode {

  List<IParamNode> getParams();

  void addParam( IParamNode param );

  List<IExceptionNode> getExceptions();

  void addException( IExceptionNode param );

  IExceptionNode getException( IJavaClassInfo exceptionClass );

}
