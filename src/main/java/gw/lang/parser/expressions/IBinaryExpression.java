package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IBinaryExpression extends IExpression
{
  IExpression getLHS();
  IExpression getRHS();
  String getOperator();
}
