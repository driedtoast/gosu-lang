package gw.fs.physical;

import gw.fs.IDirectory;
import gw.fs.IDirectoryUtil;
import gw.fs.IFile;
import gw.fs.ResourcePath;
import gw.lang.UnstableAPI;
import gw.fs.IResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class PhysicalDirectoryImpl extends PhysicalResourceImpl implements IDirectory {

  public PhysicalDirectoryImpl(ResourcePath path, IPhysicalFileSystem backingFileSystem) {
    super(path, backingFileSystem);
  }

  @Override
  public void clearCaches() {
    // No-op at this level
  }

  @Override
  public IDirectory dir(String relativePath) {
    ResourcePath absolutePath = _path.join(relativePath);
    // TODO - AHK - What if it doesn't exist?
    // TODO - AHK - What if it's not a directory?
    // TODO - AHK - What if it's in a jar file or something?
    return new PhysicalDirectoryImpl(absolutePath, _backingFileSystem);
  }

  @Override
  public IFile file(String path) {
    ResourcePath absolutePath = _path.join(path);
    // TODO - AHK - What if it doesn't exist?
    // TODO - AHK - What if it's not a file?
    return new PhysicalFileImpl(absolutePath, _backingFileSystem);
  }

  @Override
  public boolean mkdir() throws IOException {
    return _backingFileSystem.mkdir(_path);
  }

  @Override
  public List<? extends IDirectory> listDirs() {
    List<IDirectory> dirs = new ArrayList<IDirectory>();
    for (IFileMetadata fm : _backingFileSystem.listFiles(_path)) {
      if (fm.isDir()) {
        dirs.add(new PhysicalDirectoryImpl(_path.join(fm.name()), _backingFileSystem));
      }
    }

    return dirs;
  }

  @Override
  public List<? extends IFile> listFiles() {
    List<IFile> files = new ArrayList<IFile>();
    for (IFileMetadata fm : _backingFileSystem.listFiles(_path)) {
      if (fm.isFile()) {
        files.add(new PhysicalFileImpl(_path.join(fm.name()), _backingFileSystem));
      }
    }

    return files;
  }

  @Override
  public String relativePath(IResource resource) {
    // TODO - AHK - Use this based on the ResourcePath objects
    return IDirectoryUtil.relativePath(this, resource);
  }

}
