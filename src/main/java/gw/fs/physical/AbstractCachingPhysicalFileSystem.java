package gw.fs.physical;

import gw.fs.ResourcePath;
import gw.lang.UnstableAPI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public abstract class AbstractCachingPhysicalFileSystem implements IPhysicalFileSystem {

  protected final IPhysicalFileSystem _delegate;

  public AbstractCachingPhysicalFileSystem(IPhysicalFileSystem delegate) {
    _delegate = delegate;
  }

  @Override
  public boolean exists(ResourcePath resourcePath) {
    return getFileMetadata(resourcePath) != null;
  }

  @Override
  public boolean delete(ResourcePath filePath) {
    return _delegate.delete(filePath);
  }

  @Override
  public boolean mkdir(ResourcePath dirPath) {
    return _delegate.mkdir(dirPath);
  }
}
