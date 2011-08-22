package gw.fs.physical;

import gw.fs.ResourcePath;
import gw.lang.UnstableAPI;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IPhysicalFileSystem {

  List<? extends IFileMetadata> listFiles(ResourcePath directoryPath);

  IFileMetadata getFileMetadata(ResourcePath filePath);

  boolean exists(ResourcePath filePath);

  boolean delete(ResourcePath filePath);

  boolean mkdir(ResourcePath dirPath);

  void clearDirectoryCaches(ResourcePath dirPath);

  void clearAllCaches();
}
