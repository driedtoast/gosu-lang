package gw.lang.parser.expressions;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IObjectInitializerExpression extends IInitializerExpression
{
  public List<? extends IInitializerAssignment> getInitializers();
}
