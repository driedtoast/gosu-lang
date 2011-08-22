package gw.lang.javadoc;

import gw.lang.UnstableAPI;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IParamNode {

  String getDescription();

  void setDescription( String value );

  String getName();

  void setName( String name );

  String getType();

  void setType( String name );

}
