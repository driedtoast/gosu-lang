package gw.lang.parser.statements;

import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IBeanMethodCallExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IBeanMethodCallStatement extends IStatement
{
  IBeanMethodCallExpression getBeanMethodCall();
}
