package gw.lang.parser.statements;

import gw.lang.parser.IStatement;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITypeLoaderStatement extends IStatement
{
  IType getTypeLoader();
}