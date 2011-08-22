package gw.lang.parser.statements;

import gw.lang.parser.IExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IReturnStatement extends ITerminalStatement
{
  IExpression getValue();
}
