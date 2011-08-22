package gw.lang.parser;

import gw.lang.PublishInGosu;
import gw.lang.Scriptable;

/**
 * Facilitates parsed element validation via annotations.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */

@PublishInGosu
public interface IUsageSiteValidator
{
  /**
   * Called after the Gosu source corresponding to the parsed element is fully parsed.
   *
   * @param pe The parsed element to validate.
   */
  @Scriptable
  public void validate( IParsedElement pe );
}
