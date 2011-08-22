package gw.fs.physical.generic;

import gw.fs.physical.IFileMetadata;
import gw.lang.UnstableAPI;

import java.io.File;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class GenericFileMetadata implements IFileMetadata {
  private File _file;

  public GenericFileMetadata(File file) {
    _file = file;
  }

  @Override
  public String name() {
    return _file.getName();
  }

  @Override
  public long length() {
    return _file.length();
  }

  @Override
  public long lastModifiedTime() {
    return _file.lastModified();
  }

  @Override
  public boolean exists() {
    return _file.exists();
  }

  @Override
  public boolean isFile() {
    return _file.isFile();
  }

  @Override
  public boolean isDir() {
    return _file.isDirectory();
  }
}
