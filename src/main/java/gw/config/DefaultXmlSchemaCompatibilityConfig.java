package gw.config;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class DefaultXmlSchemaCompatibilityConfig extends BaseService implements IXmlSchemaCompatibilityConfig {

  @Override
  public boolean useCompatibilityMode(String namespace) {
    return false;
  }

  @Override
  protected void doInit() {
     // nothing to do
  }

}
