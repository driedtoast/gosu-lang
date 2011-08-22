package gw.lang.parser.expressions;

import gw.lang.reflect.IType;
import gw.lang.parser.IExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IArrayAccessExpression extends IExpression
{
  IExpression getRootExpression();

  IExpression getMemberExpression();

  IType getComponentType();
}
