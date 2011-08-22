package gw.fs;

import gw.lang.UnstableAPI;

import java.io.IOException;
import java.io.File;
import java.net.URI;

/**
 * NOTE: This API is in flux.  This class may be moved to a different package, renamed, or have its methods change
 * in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IResource {

  /**
   * Gets this file's our directory's parent directory.
   * @return this resource's parent directory
   */
  IDirectory getParent();

  /**
   * Gets this file's or directory's name.
   * @return this resource's name
   */
  String getName();

  /**
   * Indicates whether this resource exists.
   * @return true if the resource exists
   */
  boolean exists();

  boolean delete() throws IOException;

  // We do not want to support this.
  /*
  String getCanonicalPath();
  */

  URI toURI();

  ResourcePath getPath();

  /**
   * Indicates whether this resource is a direct child of the given directory.
   * @param dir the directory which would be the parent
   * @return true if this is a direct child of the given directory
   */
  boolean isChildOf(IDirectory dir);

  /**
   * Indicates whether this resource is a descendant of the given directory.
   * @param dir the directory which would be the ancestor
   * @return true if this is a descendant of the given directory
   */
  boolean isDescendantOf(IDirectory dir);

  File toJavaFile();

  boolean isJavaFile();
}
