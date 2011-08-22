package gw.fs;

import gw.lang.UnstableAPI;

import java.io.IOException;
import java.util.List;

/**
 * NOTE: This API is in flux.  This class may be moved to a different package, renamed, or have its methods change
 * in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IDirectory extends IResource {

  IDirectory dir(String relativePath);

  /**
   * Constucts a file given the path.  If the path is relative path,
   * it will be constructed based on the current directory
   *
   * @param path the path of the file
   * @return The file that is under the directory with the name
   */
  IFile file(String path);

  boolean mkdir() throws IOException;

  List<? extends IDirectory> listDirs();

  List<? extends IFile> listFiles();

  // TODO - AHK - Marked for deletion
  String relativePath(IResource resource);

  // TODO - AHK - Marked for deletion
  void clearCaches();
}
