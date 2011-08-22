package gw.lang.parser.statements;

import gw.lang.parser.IExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IThrowStatement extends ITerminalStatement
{
  IExpression getExpression();
}
