package gw.lang.cli;

import gw.internal.ext.org.apache.commons.cli.Option;

/**
 * This is our custom extension to {@link Option} that allows us to label an option as "hidden".
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class GosuOption extends Option {

  public GosuOption(String opt, String description) throws IllegalArgumentException {
    super(opt, description);
  }

  public GosuOption(String opt, String longOpt, boolean hasArg, String description) throws IllegalArgumentException {
    super(opt, longOpt, hasArg, description);
  }

  private boolean _hidden = false;

  /**
   * Set this option as hidden
   *
   * @param value hidden value
   */
  public void setHidden( Boolean value ) {
    _hidden = value;
  }

  /**
   * Is this a hidden option?
   * 
   * @return whether this option is hidden
   */
  public Boolean isHidden() {
    return _hidden;
  }
}
