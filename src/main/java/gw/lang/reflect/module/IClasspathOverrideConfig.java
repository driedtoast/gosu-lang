package gw.lang.reflect.module;

import gw.fs.IDirectory;
import gw.lang.PublishInGosu;
import gw.lang.Scriptable;

/**
 * This class will be removed in a future release of Gosu and replaced
 * by a more formalized version of modules
 * @deprecated will be removed and replace by more formal module
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@PublishInGosu
public interface IClasspathOverrideConfig
{

  /**
   * Returns a context token for the given file that the config will receive when it is
   * asked if another version of this file should override this version.
   */
  @Scriptable
  public String getContextToken( IDirectory path );

  /**
   * @param possibleOverride
   * @param contextToOverride
   * @return true if a file represented by the possibleOverride token should override the token represented by
   *         contextToOverride (the tokens are modules in the default Guidewire implementation)
   */
  @Scriptable
  public boolean shouldOverride( String possibleOverride, String contextToOverride );

}

