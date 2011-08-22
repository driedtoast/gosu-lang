package gw.lang.parser.statements;

import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.IStatement;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IClassFileStatement extends IStatement
{
  IClassStatement getClassStatement();

  IGosuClass getGosuClass();
    
}
