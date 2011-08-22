package gw.lang.javadoc;

import gw.lang.UnstableAPI;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IBaseFeatureNode {
  String getDescription();

  void setDescription( String value );

  String getDeprecated();

  boolean isDeprecated();

  void setDeprecated( String value );

}
