package gw.lang.parser.expressions;

import gw.lang.parser.IHasArguments;
import gw.lang.parser.IExpression;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.tidb.IFeatureInfoRecord;

import java.util.Stack;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IBeanMethodCallExpression extends IMemberAccessExpression, IHasArguments
{
  IFunctionType getFunctionType();

  IType[] getArgTypes();

  IExpression[] getArgs();

  IMethodInfo getMethodDescriptor();

  IMethodInfo getGenericMethodDescriptor();

  IFeatureInfoRecord makeFeatureInfoRecord( Stack<IFeatureInfo> iFeatureInfos, IFeatureInfoRecord.DefUse read );
}
