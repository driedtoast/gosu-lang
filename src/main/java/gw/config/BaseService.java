package gw.config;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class BaseService implements IService
{
  private boolean _inited = false;

  public final boolean isInited()
  {
    return _inited;
  }

  public final void init()
  {
    doInit();
    _inited = true;
  }

  protected abstract void doInit();
}