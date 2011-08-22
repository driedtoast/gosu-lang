package gw.lang.shell;

import java.net.URL;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IUpdateableClassLoader {

  /**
   * Add a URL to resolve classes against
   */
  void addURL(URL url);

}
