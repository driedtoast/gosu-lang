package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IParenthesizedExpression extends IExpression
{
  IExpression getExpression();
}
