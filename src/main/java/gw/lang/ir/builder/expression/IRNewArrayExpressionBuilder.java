package gw.lang.ir.builder.expression;

import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.IRType;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRNewArrayExpression;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRNewArrayExpressionBuilder extends IRExpressionBuilder {

  private IRType _componentType;
  private IRExpressionBuilder _size;

  public IRNewArrayExpressionBuilder(IRType componentType, IRExpressionBuilder size ) {
    _componentType = componentType;
    _size = size;
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return new IRNewArrayExpression(_componentType, _size.build(context) );
  }
}
