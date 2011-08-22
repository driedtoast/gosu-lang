package gw.lang.init;

import gw.fs.IDirectory;
import gw.lang.UnstableAPI;

import java.util.List;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * A GosuPathEntry represents a component in the path used to initialize the Gosu runtime.  Each path entry consists
 * of a root directory, any number of source directories, and any number of typeloader names.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class GosuPathEntry {

  private IDirectory _root;
  private List<? extends IDirectory> _srcs;
  private List<String> _typeloaders;

  /**
   * Constructs a new GosuPathEntry with the specified data.  None of the arguments
   * are allowed to be null.  Typeloader names should be fully-qualified Java class names.
   *
   * @param root the root IDirectory for this path entry
   * @param srcs the set of IDirectories for this entry that should be considered to be source directories
   * @param typeloaders the set of custom typeloaders that should be instantiated with this set of resources
   */
  public GosuPathEntry(IDirectory root, List<? extends IDirectory> srcs, List<String> typeloaders) {
    if (root == null) {
      throw new IllegalArgumentException("The root argument cannot be null");
    }
    if (srcs == null) {
      throw new IllegalArgumentException("The srcs argument cannot be null");
    }
    if (typeloaders == null) {
      throw new IllegalArgumentException("The typeloaders argument cannot be null");
    }
    _root = root;
    _srcs = srcs;
    _typeloaders = typeloaders;
  }

  /**
   * Returns the root directory for this GosuPathEntry.  This method will never return null.
   *
   * @return the root directory
   */
  public IDirectory getRoot() {
    return _root;
  }

  /**
   * Returns the source directories for this path entry.  This method will never return null.
   *
   * @return the source directories
   */
  public List<? extends IDirectory> getSources() {
    return _srcs;
  }

  /**
   * Returns the typeloaders for this path entry.  This method will never return null.
   *
   * @return the fully-qualified names of the typeloaders
   */
  public List<String> getTypeloaders() {
    return _typeloaders;
  }

  /**
   * Returns a String representation of this path entry suitable for use in debugging.
   *
   * @return a debug String representation of this object
   */
  public String toDebugString() {
    StringBuilder sb = new StringBuilder();
    sb.append("GosuPathEntry:\n");
    sb.append("  root: ").append(_root.toJavaFile().getAbsolutePath()).append("\n");
    for (IDirectory src : _srcs) {
      sb.append("  src: ").append(src.toJavaFile().getAbsolutePath()).append("\n");
    }
    for (String loader : _typeloaders) {
      sb.append("  loader: ").append(loader).append("\n");
    }
    return sb.toString();
  }
}