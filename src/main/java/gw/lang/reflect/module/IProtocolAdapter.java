package gw.lang.reflect.module;

import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.UnstableAPI;

import java.net.URL;

/**
 * NOTE: This API is in flux.  This class may be moved to a different package, renamed, or have its methods change
 * in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IProtocolAdapter {
  String getProtocol();

  IDirectory getIDirectory(URL url);

  IFile getIFile(URL url);

}
