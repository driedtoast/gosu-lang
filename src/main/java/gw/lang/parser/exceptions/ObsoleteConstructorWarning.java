package gw.lang.parser.exceptions;

import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.IParserState;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ObsoleteConstructorWarning extends ParseWarning
{
  public ObsoleteConstructorWarning( IParserState standardParserState, ResourceKey msgObsoleteCtorSyntax )
  {
    super( standardParserState, msgObsoleteCtorSyntax );
  }
}
