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
public class LibraryContainer implements IResourceContainer
{
  /**
   * A Jar file or folder
   */
  protected IDirectory _file;

  public LibraryContainer( IDirectory file )
  {
    _file = file;
  }

  public ContainerType getContainerType()
  {
    return ContainerType.LIBRARY;
  }
  
  public IDirectory getOutputPath()
  {
    return _file;
  }
  public void setOutputPath( IDirectory path )
  {
    _file = path;
  }  
}
