package gw.lang.parser.exceptions;

import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.IParserState;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ImplicitCoercionWarning extends ParseWarning implements ICoercionIssue
{
  private IType _typeToCoerceTo;

  public ImplicitCoercionWarning( IParserState standardParserState,
                                  ResourceKey msgImplicitCoercionWarning,
                                  IType typeToCoerceTo,
                                  Object... args )
  {
    super( standardParserState, msgImplicitCoercionWarning, args );
    _typeToCoerceTo = typeToCoerceTo;
  }

  public IType getTypeToCoerceTo()
  {
    return _typeToCoerceTo;
  }
}
