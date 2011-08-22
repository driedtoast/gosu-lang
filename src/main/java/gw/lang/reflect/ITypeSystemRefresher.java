package gw.lang.reflect;

import gw.lang.UnstableAPI;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface ITypeSystemRefresher
{
  void maybeRefresh();

  void maybeRefresh(boolean force);
}