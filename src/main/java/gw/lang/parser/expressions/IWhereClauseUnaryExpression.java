package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IWhereClauseUnaryExpression extends IExpression, IQueryPartAssembler
{
  IExpression getExpression();
}
