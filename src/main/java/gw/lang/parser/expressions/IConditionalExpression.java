package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IConditionalExpression extends IExpression, IOverridableOperation
{
  IExpression getLHS();

  IExpression getRHS();
}
