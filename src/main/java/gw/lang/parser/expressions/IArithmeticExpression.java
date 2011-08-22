package gw.lang.parser.expressions;

import gw.lang.parser.IParsedElement;
import gw.lang.parser.IExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IArithmeticExpression extends IParsedElement, IOverridableOperation
{
  IExpression getLHS();

  IExpression getRHS();

  String getOperator();
}
