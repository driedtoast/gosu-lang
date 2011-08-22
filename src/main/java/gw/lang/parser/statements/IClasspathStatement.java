package gw.lang.parser.statements;

import gw.lang.parser.IStatement;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IClasspathStatement extends IStatement
{
  String getClasspath();

  void setClasspath( String classpath );

  List<String> getPaths();
}