package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IHasArguments;
import gw.lang.reflect.IFunctionType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IMethodCallExpression extends IExpression, IHasArguments
{
  IFunctionSymbol getFunctionSymbol();

  IExpression[] getArgs();

  int getArgPosition();

  IFunctionType getFunctionType();
}
