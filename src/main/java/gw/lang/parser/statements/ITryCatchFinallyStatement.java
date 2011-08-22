package gw.lang.parser.statements;

import gw.lang.parser.IStatement;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITryCatchFinallyStatement extends IStatement
{
  IStatement getTryStatement();

  List<? extends ICatchClause> getCatchStatements();

  IStatement getFinallyStatement();
}
