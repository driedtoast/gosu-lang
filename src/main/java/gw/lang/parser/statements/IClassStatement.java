package gw.lang.parser.statements;

import gw.lang.parser.IStatement;
import gw.lang.reflect.gs.IGosuClass;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IClassStatement extends IStatement
{
  IClassFileStatement getClassFileStatement();

  IGosuClass getGosuClass();

  IClassDeclaration getClassDeclaration();
}
