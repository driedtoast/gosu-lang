package gw.fs;

import gw.fs.jar.IJarFileDirectory;
import gw.lang.UnstableAPI;

import java.util.List;
import java.util.ArrayList;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IDirectoryUtil {
  // TODO - AHK - Most of this class needs to die

  public static List<? extends IFile> allContainedFiles(IDirectory dir) {
    List<IFile> files = new ArrayList<IFile>();
    addContainedFiles(dir, files);
    return files;
  }

  private static void addContainedFiles(IDirectory dir, List<IFile> files) {
    files.addAll(dir.listFiles());
    for (IDirectory subDir : dir.listDirs()) {
      addContainedFiles(subDir, files);
    }
  }

  public static List<String> splitPath(String relativePath) {
    List<String> results = new ArrayList<String>();
    char first = relativePath.charAt(0);
    if (first == '/' || first=='\\') {
      relativePath = relativePath.substring(1);
    }
    char last = relativePath.charAt(relativePath.length() - 1);
    if (last == '/' || last == '\\') {
      relativePath = relativePath.substring(0, relativePath.length() - 1);
    }
    int lastIndex = 0;
    for (int i = 0; i < relativePath.length(); i++) {
      char c = relativePath.charAt(i);
      if (c == '/' || c == '\\') {
        results.add(relativePath.substring(lastIndex, i));
        lastIndex = i + 1;
      }
    }

    String pathString = relativePath.substring(lastIndex);
    // Discard single dots at this point; they don't impact the path at all, so just throw them out
    if (!".".equals(pathString)) {
      results.add(pathString);
    }

    return results;
  }

  public static String relativePath(IResource root, IResource resource) {
    return root.getPath().relativePath(resource.getPath(), "/");
//    String rootPath;
//    String resourcePath;
//    // DNW:  Have to fix this to make perf actions work.  See also change 343639.
//    try {
//	rootPath = normalizePath(new File(root.getAbsolutePath()).getCanonicalPath());
//        resourcePath = normalizePath(new File(resource.getAbsolutePath()).getCanonicalPath());
//    }
//    catch (IOException e){
//      throw new RuntimeException(e);
//    }
//    // TODO - AHK - Use canonical paths
//    if (resourcePath.startsWith(rootPath)) {
//      if (rootPath.endsWith("/")) {
//        return resourcePath.substring(rootPath.length());
//      } else {
//        return resourcePath.substring(rootPath.length() + 1);
//      }
//    } else {
//      return null;
//    }
  }

  private static String normalizePath(String path) {
    return path.replace('\\', '/');
  }
  
  public static IDirectory dir(IJarFileDirectory root, String relativePath) {
    List<String> pathComponents = IDirectoryUtil.splitPath(relativePath);
    if (pathComponents.size() == 0) {
      return root;
    } else if (pathComponents.size() == 1) {
      return root.getOrCreateDirectory(pathComponents.get(0));
    } else {
      return findParentDirectory(root, pathComponents);
    }
  }

  public static IFile file(IJarFileDirectory root, String path) {
    List<String> pathComponents = IDirectoryUtil.splitPath(path);
    if (pathComponents.size() == 0) {
      throw new IllegalArgumentException("Cannot call file() with an empty path");
    } else if (pathComponents.size() == 1) {
      return root.getOrCreateFile(pathComponents.get(0));
    } else {
      String fileName = pathComponents.remove(pathComponents.size() - 1);
      IDirectory parentDir = findParentDirectory(root, pathComponents);
      return parentDir.file(fileName);
    }
  }

  private static IDirectory findParentDirectory(IDirectory root, List<String> relativePath) {
    IDirectory parent = root;
    for (String pathComponent : relativePath) {
      if (pathComponent.equals(".")) {
        // Do nothing
      } else if (pathComponent.equals("..")) {
        parent = parent.getParent();
      } else {
        parent = parent.dir(pathComponent);
      }
    }
    return parent;
  }

}
