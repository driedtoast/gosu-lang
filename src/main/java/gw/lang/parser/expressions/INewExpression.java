package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IHasArguments;
import gw.lang.reflect.IType;
import gw.lang.reflect.IConstructorInfo;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface INewExpression extends IExpression, IHasArguments
{
  IType[] getArgTypes();

  IExpression[] getArgs();

  IConstructorInfo getConstructor();

  List<? extends IExpression> getValueExpressions();

  IInitializerExpression getInitializer();

  List<? extends IExpression> getSizeExpressions();

  boolean isAnonymousClass();

  ITypeLiteralExpression getTypeLiteral();
}
