package gw.config;

import gw.lang.reflect.ITypeLoader;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuInitializationHooks extends IService {
  void beforeTypeLoaderCreation(Class typeLoaderClass);
  void afterTypeLoaderCreation(ITypeLoader typeLoader);
}