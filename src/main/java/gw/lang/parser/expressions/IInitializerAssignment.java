package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;
import gw.lang.reflect.IPropertyInfo;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IInitializerAssignment extends IStatement
{
  public String getPropertyName();
  public IPropertyInfo getPropertyInfo();
  public IExpression getRhs();
}
