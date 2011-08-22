package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITypeLiteralExpression extends ILiteralExpression, Cloneable
{
  IMetaType getType();
  void setType( IType strFqn );

  IExpression getPackageExpression();
  int getRelativeTypeStart();
  int getRelativeTypeEnd();
}
