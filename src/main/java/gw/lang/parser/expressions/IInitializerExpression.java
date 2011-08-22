package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IInitializerExpression extends IExpression
{
  public void initialize( Object newObject );
}