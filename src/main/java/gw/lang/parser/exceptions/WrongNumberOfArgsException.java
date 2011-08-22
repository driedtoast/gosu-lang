package gw.lang.parser.exceptions;

import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.IParserState;
import gw.lang.parser.CaseInsensitiveCharSequence;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class WrongNumberOfArgsException extends ParseException
{
  public WrongNumberOfArgsException( IParserState standardParserState, ResourceKey msgWrongNumberOfArgsToFunction, CaseInsensitiveCharSequence paramSignature, int expectedArgs, int iArgs) {
    super(standardParserState, msgWrongNumberOfArgsToFunction, paramSignature, expectedArgs, iArgs);
  }
}
