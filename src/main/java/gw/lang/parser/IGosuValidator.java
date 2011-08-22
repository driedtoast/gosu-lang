package gw.lang.parser;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuValidator
{
  /**
   * Provides additional semantic checks to a Gosu parser
   *
   * @param rootParsedElement - the root parsed element to validate from
   */
  void validate( IParsedElement rootParsedElement, String scriptSrc );
}