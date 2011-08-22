package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ICaseClause
{
  IExpression getExpression();

  List<? extends IStatement> getStatements();
}
