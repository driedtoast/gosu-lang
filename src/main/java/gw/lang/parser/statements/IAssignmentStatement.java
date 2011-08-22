package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IIdentifierExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IAssignmentStatement extends IStatement
{
  IIdentifierExpression getIdentifier();

  IExpression getExpression();
}
