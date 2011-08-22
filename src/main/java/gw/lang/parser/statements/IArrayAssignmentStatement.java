package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IArrayAccessExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IArrayAssignmentStatement extends IStatement
{
  IArrayAccessExpression getArrayAccessExpression();

  IExpression getExpression();
}
