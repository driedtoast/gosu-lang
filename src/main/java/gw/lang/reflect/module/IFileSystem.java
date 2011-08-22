package gw.lang.reflect.module;

import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.UnstableAPI;

import java.io.File;
import java.net.URL;

/**
 * NOTE: This API is in flux.  This class may be moved to a different package, renamed, or have its methods change
 * in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IFileSystem {

  IDirectory getIDirectory(File dir);

  IFile getIFile(File file);

  void setCachingMode(CachingMode cachingMode);

  void clearAllCaches();

  IDirectory getIDirectory(URL url);

  IFile getIFile( URL url );

  public enum CachingMode {
    NO_CACHING,
    CHECK_TIMESTAMPS,
    FUZZY_TIMESTAMPS,
    FULL_CACHING
  }
}
