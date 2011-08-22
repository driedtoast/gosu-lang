package gw.lang.javadoc;

import gw.lang.UnstableAPI;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IJavaDocFactory {

  IClassDocNode create( Class javaClass );

  IClassDocNode create();

  /**
   * @return a new, empty IParamNode
   * @deprecated Please don't create these manually, and please fix any code that does.
   */
  @Deprecated
  IParamNode createParam();

  /**
   * @return a new, empty IExceptionNode
   * @deprecated Please don't create these manually, and please fix any code that does.
   */
  @Deprecated
  IExceptionNode createException();

}
