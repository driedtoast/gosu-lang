package gw.lang.parser.expressions;

import gw.lang.reflect.IType;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IDelegateStatement extends IVarStatement
{
  
  List<IType> getConstituents();

}
