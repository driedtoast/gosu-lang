package gw.fs.physical;

import gw.fs.IDirectory;
import gw.fs.IResource;
import gw.fs.ResourcePath;
import gw.lang.UnstableAPI;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class PhysicalResourceImpl implements IResource {
  protected final ResourcePath _path;
  protected final IPhysicalFileSystem _backingFileSystem;

  protected PhysicalResourceImpl(ResourcePath path, IPhysicalFileSystem backingFileSystem) {
    _path = path;
    _backingFileSystem = backingFileSystem;
  }

  @Override
  public IDirectory getParent() {
    if (_path.getParent() == null) {
      return null;
    } else {
      return new PhysicalDirectoryImpl(_path.getParent(), _backingFileSystem);
    }
  }

  @Override
  public String getName() {
    return _path.getName();
  }

  @Override
  public boolean exists() {
    return getIFileMetadata().exists();
  }

  @Override
  public boolean delete() throws IOException {
    return _backingFileSystem.delete(_path);
  }

  @Override
  public URI toURI() {
    // TODO - AHK - Where does this properly belong?
    return toJavaFile().toURI();
  }

  @Override
  public ResourcePath getPath() {
    return _path;
  }

  @Override
  public boolean isChildOf(IDirectory dir) {
    return dir.getPath().isChild(_path);
  }

  @Override
  public boolean isDescendantOf( IDirectory dir ) {
    return dir.getPath().isDescendant(_path);
  }

  @Override
  public File toJavaFile() {
    return new File(_path.getPathString());
  }

  @Override
  public boolean isJavaFile() {
    return true;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof IResource) {
      return _path.equals(((IResource) obj).getPath());
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return getPath().getFileSystemPathString();
  }
  
  protected IFileMetadata getIFileMetadata() {
    return _backingFileSystem.getFileMetadata(_path);
  }
}
