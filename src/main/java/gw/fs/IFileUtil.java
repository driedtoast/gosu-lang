package gw.fs;

import gw.lang.UnstableAPI;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IFileUtil {

  public static String getExtension(IFile file) {
    int lastDot = file.getName().lastIndexOf(".");
    if (lastDot >= 0) {
      return file.getName().substring(lastDot + 1);
    }
    else {
      return "";
    }
  }

  public static String getBaseName(IFile file) {
    int lastDot = file.getName().lastIndexOf(".");
    if (lastDot >= 0) {
      return file.getName().substring(0, lastDot);
    }
    else {
      return "";
    }
  }
}
