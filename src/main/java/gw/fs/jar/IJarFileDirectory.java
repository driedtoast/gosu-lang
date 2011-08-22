package gw.fs.jar;

import gw.fs.IDirectory;
import gw.lang.UnstableAPI;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IJarFileDirectory extends IDirectory {
  JarEntryDirectoryImpl getOrCreateDirectory(String relativeName);
  JarEntryFileImpl getOrCreateFile(String relativeName);
}
