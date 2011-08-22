package gw.lang.reflect.module;

import gw.fs.IDirectory;
import gw.lang.UnstableAPI;

/**
 * NOTE: This API is in flux.  This class may be moved to a different package, renamed, or have its methods change
 * in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IResourceContainer
{
  static enum ContainerType
  {
    MODULE,
    LIBRARY,
  }
  
  ContainerType getContainerType();
  
  IDirectory getOutputPath();
  void setOutputPath( IDirectory path );
}
