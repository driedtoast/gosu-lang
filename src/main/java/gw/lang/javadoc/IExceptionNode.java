package gw.lang.javadoc;

import gw.lang.UnstableAPI;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IExceptionNode {

  String getDescription();

  void setDescription( String value );

  String getType();

  void setType( String name );
  
}
