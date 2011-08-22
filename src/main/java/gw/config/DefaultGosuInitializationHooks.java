package gw.config;

import gw.lang.reflect.ITypeLoader;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class DefaultGosuInitializationHooks extends BaseService implements IGosuInitializationHooks
{

  protected void doInit() {
  }

  @Override
  public void beforeTypeLoaderCreation(Class typeLoaderClass) {
    // Do nothing
  }

  @Override
  public void afterTypeLoaderCreation(ITypeLoader typeLoader) {
    // Do nothing
  }

}