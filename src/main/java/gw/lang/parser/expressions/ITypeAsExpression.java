package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.ICoercer;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITypeAsExpression extends IExpression
{
  IExpression getLHS();
  ICoercer getCoercer();
}
