package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.reflect.IPropertyInfo;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IFieldAccessExpression extends IMemberAccessExpression
{
  IPropertyInfo getPropertyInfo();

  IExpression getMemberExpression();
  
  public String getMethodNameForSyntheticAccess();
}
