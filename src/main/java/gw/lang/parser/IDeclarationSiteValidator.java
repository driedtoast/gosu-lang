package gw.lang.parser;

import gw.lang.PublishInGosu;
import gw.lang.Scriptable;

/**
 * Annotation interface that allows the annotation to participate in validation of the parse tree
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@PublishInGosu
public interface IDeclarationSiteValidator
{
  /**
   * Called after the whole class has been defn compiled.
   * Implementors of this method can inspect the parse tree and add warnings/errors as appropriate
   *
   * @param feature the parsed element that this annotation lives on.
   */
  @Scriptable
  public void validate( IParsedElement feature );
}
