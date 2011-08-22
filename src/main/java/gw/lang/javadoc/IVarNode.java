package gw.lang.javadoc;

import gw.lang.UnstableAPI;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IVarNode extends IBaseFeatureNode {

  String getName();

  void setName( String name );

}
