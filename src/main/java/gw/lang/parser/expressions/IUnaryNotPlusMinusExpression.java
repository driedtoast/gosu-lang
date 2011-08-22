package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IUnaryNotPlusMinusExpression extends IExpression
{
  boolean isNot();

  boolean isBitNot();

  IExpression getExpression();
}
