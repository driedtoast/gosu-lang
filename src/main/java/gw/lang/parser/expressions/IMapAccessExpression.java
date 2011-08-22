package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IMapAccessExpression extends IExpression
{
  IExpression getRootExpression();

  IExpression getKeyExpression();

  IType getComponentType();

  IType getKeyType();
}
