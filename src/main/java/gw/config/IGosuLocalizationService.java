package gw.config;

import gw.lang.parser.resources.ResourceKey;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuLocalizationService extends IService
{

  public String localize( ResourceKey key, Object... args );

  public boolean exists( ResourceKey key );

}