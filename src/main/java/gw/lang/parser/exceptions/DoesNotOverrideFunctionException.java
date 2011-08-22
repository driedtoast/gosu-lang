package gw.lang.parser.exceptions;

import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.IParserState;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class DoesNotOverrideFunctionException extends ParseException
{
  public DoesNotOverrideFunctionException( IParserState standardParserState, ResourceKey resourceKey, CharSequence... functionName) {
    super(standardParserState, resourceKey, functionName);
  }
}
