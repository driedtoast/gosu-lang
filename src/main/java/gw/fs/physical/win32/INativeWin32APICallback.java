package gw.fs.physical.win32;

import gw.lang.UnstableAPI;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface INativeWin32APICallback {
  void handleFile(String name, int attributes, long createTime, long lastAccessTime, long lastModifiedTime, long length);
}
