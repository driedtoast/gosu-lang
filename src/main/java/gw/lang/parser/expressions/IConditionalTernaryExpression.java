package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IConditionalTernaryExpression extends IExpression
{
  IExpression getCondition();

  IExpression getFirst();

  IExpression getSecond();
}
