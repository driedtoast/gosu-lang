package gw.lang.parser.expressions;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IWhereClauseRelationalExpression extends IConditionalExpression, IQueryPartAssembler
{
  String getOperator();
}
