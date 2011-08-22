package gw.lang.parser.statements;

import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IEvalExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IEvalStatement extends IStatement
{
  IEvalExpression getEvalExpression();
}