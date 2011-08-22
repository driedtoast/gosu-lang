package gw.lang.parser;

import gw.lang.reflect.gs.IGosuProgram;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IParseResult extends IHasType
{
  IExpression getExpression();
  IStatement getStatement();

  IExpression getRawExpression();
  IGosuProgram getProgram();

  IParsedElement getParsedElement();

  Object evaluate();

  boolean isLiteral();
  boolean isProgram();
}