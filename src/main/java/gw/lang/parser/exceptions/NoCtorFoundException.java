package gw.lang.parser.exceptions;

import gw.lang.parser.resources.Res;
import gw.lang.parser.IFullParserState;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class NoCtorFoundException extends ParseException
{
  public NoCtorFoundException( IFullParserState parserState, Object... args )
  {
    super( parserState, Res.MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS, args );
  }
}
