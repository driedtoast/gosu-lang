package gw.lang.reflect.module;

import gw.fs.IFile;
import gw.fs.IDirectory;
import gw.fs.IResource;
import gw.lang.UnstableAPI;
import gw.util.Pair;

import java.net.URL;
import java.util.List;

/**
 * NOTE: This API is in flux.  This class may be moved to a different package, renamed, or have its methods change
 * in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IResourceAccess {

  List<? extends IDirectory> getRoots();

  List<? extends IDirectory> getSourceEntries();

  IFile findFirstFile(String resourceName);

  List<? extends IFile> findAllFiles(String resourceName);

  List<Pair<String, IFile>> findAllFilesByExtension(String extension);

  IResourceAccess clearCaches();
  /**
   * Iterates unique files within the specified directory, relative to the root directories.
   * 
   * @param relativeDirName
   * @return
   */
  Iterable<? extends IFile> iterateUniqueFilesWithinDirectory( String relativeDirName ); 

  String pathRelativeToRoot(IResource resource);

  String getResourceName(URL url);
}