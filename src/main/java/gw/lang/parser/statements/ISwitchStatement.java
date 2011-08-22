package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ISwitchStatement extends IStatement
{
  IExpression getSwitchExpression();

  ICaseClause[] getCases();

  List<? extends IStatement> getDefaultStatements();
}
