package gw.lang.parser.statements;

import gw.lang.parser.IHasArguments;
import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IMethodCallExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IMethodCallStatement extends IStatement, IHasArguments
{
  IMethodCallExpression getMethodCall();
}
