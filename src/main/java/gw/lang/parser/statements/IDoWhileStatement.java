package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IDoWhileStatement extends ILoopStatement
{
  IExpression getExpression();

  IStatement getStatement();
}
