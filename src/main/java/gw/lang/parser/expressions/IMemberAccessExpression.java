package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IExpressionRuntime;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IMemberAccessExpression extends IExpression
{
  IExpression getRootExpression();

  IType getRootType();
  
  String getMemberName();

  int getStartOffset();
  void setStartOffset( int iOffset );

  void setExpressionRuntime(IExpressionRuntime expressionRuntime);

  IExpressionRuntime getExpressionRuntime( );
}
