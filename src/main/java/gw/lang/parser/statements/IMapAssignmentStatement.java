package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IMapAccessExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IMapAssignmentStatement extends IStatement
{
  IMapAccessExpression getMapAccessExpression();

  IExpression getExpression();
}
