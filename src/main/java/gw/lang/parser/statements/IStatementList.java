package gw.lang.parser.statements;

import gw.lang.parser.IStatement;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IStatementList extends IStatement
{
  IStatement[] getStatements();

  boolean hasScope();
}
