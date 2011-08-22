package gw.config;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLDecoder;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ResourceFileResolver {

  public File resolveToFile(String fileName) {
    return resolveToFile(fileName, getClass().getClassLoader());
  }

  public File resolveToFile(String fileName, ClassLoader classLoader) {
    URL url = classLoader.getResource(fileName);
    return resolveURLToFile(fileName, url);
  }

  public File resolveURLToFile(String fileName, URL url) {
    if (url == null) {
      return null;
    }
    String urlFile = URLDecoder.decode(url.getFile());
    if (urlFile.startsWith("file:/")) {
      urlFile = urlFile.substring(6, urlFile.length() - fileName.length() - 2); // Windows style
      if (!new File(urlFile).exists()) {
        urlFile = url.getFile().substring(5, url.getFile().length() - fileName.length() - 2); // Unix style
      }
    } else if (urlFile.startsWith("file:")) {
      urlFile = urlFile.substring(5, urlFile.length() - fileName.length() - 2); // Windows style
    } else {
      urlFile = urlFile.substring(1, urlFile.length() - fileName.length()); // Windows style
      if (!new File(urlFile).exists()) {
        urlFile = url.getFile().substring(0, url.getFile().length() - fileName.length()); // Unix style (e.g. /home/foo/bar/Class.gs)
      }
    }
    return new File(urlFile);
  }

}
