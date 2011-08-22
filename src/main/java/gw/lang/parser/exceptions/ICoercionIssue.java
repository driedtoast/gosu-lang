package gw.lang.parser.exceptions;

import gw.lang.reflect.IType;
import gw.lang.parser.IParseIssue;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ICoercionIssue extends IParseIssue
{
  public IType getTypeToCoerceTo();

  public String getContextStringNoLineNumbers();
}
