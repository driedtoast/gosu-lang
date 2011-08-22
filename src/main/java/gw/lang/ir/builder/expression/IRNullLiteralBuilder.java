package gw.lang.ir.builder.expression;

import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRNullLiteral;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRNullLiteralBuilder extends IRExpressionBuilder {
  public IRNullLiteralBuilder() {
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return new IRNullLiteral();
  }
}
