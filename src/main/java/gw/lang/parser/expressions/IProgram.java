package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;
import gw.lang.parser.IManagedContext;

import java.util.Map;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IProgram extends IExpression
{
  IStatement getMainStatement();

  Map getFunctions();

  boolean hasContent();

  void setManagedContext( IManagedContext ctx );
}
