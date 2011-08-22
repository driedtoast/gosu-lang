package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IIfStatement extends IStatement
{
  IExpression getExpression();

  IStatement getStatement();

  IStatement getElseStatement();

  boolean hasElseStatement();
}
