package gw.fs.jar;

import gw.fs.IDirectory;
import gw.fs.IDirectoryUtil;
import gw.fs.IResource;
import gw.fs.ResourcePath;
import gw.lang.UnstableAPI;
import gw.fs.IFile;
import gw.config.CommonServices;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.net.URI;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class JarFileDirectoryImpl implements IJarFileDirectory {

  private File _file;
  private JarFile _jarFile;
  private Map<String, IResource> _resources;
  private List<IDirectory> _childDirs;
  private List<IFile> _childFiles;

  public JarFileDirectoryImpl(File file) {
    _resources = new HashMap<String, IResource>();
    _childFiles = new ArrayList<IFile>();
    _childDirs = new ArrayList<IDirectory>();
    _file = file;

    if (file.exists()) {
      try {
        _jarFile = new JarFile(file);
        Enumeration<JarEntry> entries = _jarFile.entries();
        while (entries.hasMoreElements()) {
          JarEntry e = entries.nextElement();
          processJarEntry(e);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void processJarEntry(JarEntry e) {
    List<String> pathComponents = IDirectoryUtil.splitPath(e.getName());
    if (pathComponents.size() == 1) {
      String name = pathComponents.get(0);
      if (e.isDirectory()) {
        JarEntryDirectoryImpl resource = getOrCreateDirectory(name);
        resource.setEntry(e);
      } else {
        JarEntryFileImpl resource = getOrCreateFile(name);
        resource.setEntry(e);
      }
    } else {
      JarEntryDirectoryImpl parentDirectory = getOrCreateDirectory(pathComponents.get(0));
      for (int i = 1; i < pathComponents.size() - 1; i++) {
        parentDirectory = parentDirectory.getOrCreateDirectory(pathComponents.get(i));
      }

      if (e.isDirectory()) {
        JarEntryDirectoryImpl leafDir = parentDirectory.getOrCreateDirectory(pathComponents.get(pathComponents.size() - 1));
        leafDir.setEntry(e);
      } else {
        JarEntryFileImpl leafFile = parentDirectory.getOrCreateFile(pathComponents.get(pathComponents.size() - 1));
        leafFile.setEntry(e);
      }
    }
  }

  public InputStream getInputStream(JarEntry entry) throws IOException {
    return _jarFile.getInputStream(entry);
  }

  // IJarFileDirectory methods

  @Override
  public JarEntryDirectoryImpl getOrCreateDirectory(String relativeName) {
    IResource resource = _resources.get(relativeName);
    if(resource instanceof IFile){
      throw new UnsupportedOperationException("The requested resource " + relativeName + " is now being accessed as a directory, but was previously accessed as a file.");
    }
    JarEntryDirectoryImpl result = (JarEntryDirectoryImpl) resource;
    if (result == null) {
      result = new JarEntryDirectoryImpl(relativeName, this, this);
      _resources.put(relativeName, result);
      _childDirs.add(result);
    }
    return result;
  }

  @Override
  public JarEntryFileImpl getOrCreateFile(String relativeName) {
    IResource resource = _resources.get(relativeName);
    if(resource instanceof IDirectory){
      throw new UnsupportedOperationException("The requested resource " + relativeName + " is now being accessed as a file, but was previously accessed as a directory.");
    }
    JarEntryFileImpl result = (JarEntryFileImpl) resource;
    if (result == null) {
      result = new JarEntryFileImpl(relativeName, this, this);
      _resources.put(relativeName, result);
      _childFiles.add(result);
    }
    return result;
  }

  // IDirectory methods

    @Override
  public IDirectory dir(String relativePath) {
    return IDirectoryUtil.dir(this, relativePath);
  }

  @Override
  public IFile file(String path) {
    return IDirectoryUtil.file(this, path);
  }

  @Override
  public boolean mkdir() throws IOException {
    // TODO - AHK - Should this just return false?
    throw new UnsupportedOperationException();
  }

  @Override
  public List<? extends IDirectory> listDirs() {
    List<IDirectory> results = new ArrayList<IDirectory>();
    for (IDirectory child : _childDirs) {
      if (child.exists()) {
        results.add(child);
      }
    }
    return results;
  }

  @Override
  public List<? extends IFile> listFiles() {
    List<IFile> results = new ArrayList<IFile>();
    for (IFile child : _childFiles) {
      if (child.exists()) {
        results.add(child);
      }
    }
    return results;
  }

  @Override
  public String relativePath(IResource resource) {
    return IDirectoryUtil.relativePath(this, resource);
  }

  @Override
  public IDirectory getParent() {
    File parentFile = _file.getParentFile();
    if (parentFile != null) {
      return CommonServices.getFileSystem().getIDirectory(parentFile);
    } else {
      return null;
    }
  }

  @Override
  public String getName() {
    return _file.getName();
  }

  @Override
  public boolean exists() {
    return _file.exists();
  }

  @Override
  public boolean delete() throws IOException {
    return _file.delete();
  }

  @Override
  public URI toURI() {
    return _file.toURI();
  }

  @Override
  public ResourcePath getPath() {
    return ResourcePath.parse(_file.getAbsolutePath());
  }

  @Override
  public boolean isChildOf(IDirectory dir) {
    return dir.equals(getParent());
  }

  @Override
  public boolean isDescendantOf(IDirectory dir) {
    return dir.getPath().isDescendant(getPath());
  }

  @Override
  public File toJavaFile() {
    return _file;
  }

  public JarFile getJarFile()
  {
    return _jarFile;
  }

  @Override
  public boolean isJavaFile() {
    return true;
  }
  
  @Override
  public String toString() {
    return getPath().toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (obj instanceof JarFileDirectoryImpl) {
      return getPath().equals(((JarFileDirectoryImpl) obj).getPath());
    } else {
      return false;
    }
  }

  @Override
  public void clearCaches() {
    // Do nothing
    // TODO - AHK - Should this do anything?
  }
}
