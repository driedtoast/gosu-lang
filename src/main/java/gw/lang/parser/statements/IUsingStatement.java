package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IVarStatement;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IUsingStatement extends IStatement
{
  IExpression getExpression();

  IStatement getStatement();

  List<IVarStatement> getVarStatements();

  boolean hasVarStatements();
}
