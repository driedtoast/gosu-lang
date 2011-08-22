package gw.lang.reflect.module;

import gw.lang.UnstableAPI;

/**
 * NOTE: This API is in flux.  This class may be moved to a different package, renamed, or have its methods change
 * in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class Dependency
{
  private IResourceContainer _module;
  private boolean _bExport;

  public Dependency( IResourceContainer module, boolean bExport )
  {
    _module = module;
    _bExport = bExport;
  }

  public IResourceContainer getResourceContainer()
  {
    return _module;
  }

  public boolean isExport()
  {
    return _bExport;
  }
}
