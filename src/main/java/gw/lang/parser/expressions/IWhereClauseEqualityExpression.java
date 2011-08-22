package gw.lang.parser.expressions;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IWhereClauseEqualityExpression extends IConditionalExpression, IQueryPartAssembler
{
  boolean isEquals();
}
