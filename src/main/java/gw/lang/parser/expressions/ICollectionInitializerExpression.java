package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ICollectionInitializerExpression extends IInitializerExpression {
  public List<IExpression> getValues();
}
