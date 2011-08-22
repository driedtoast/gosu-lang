package gw.fs.physical;

import gw.lang.UnstableAPI;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IFileMetadata {
  String name();
  boolean isDir();
  boolean isFile();
  boolean exists();
  long lastModifiedTime();
  long length();
}
