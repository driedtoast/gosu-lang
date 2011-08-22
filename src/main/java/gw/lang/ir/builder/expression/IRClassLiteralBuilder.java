package gw.lang.ir.builder.expression;

import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.expression.IRClassLiteral;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRClassLiteralBuilder extends IRExpressionBuilder {

  private IRType _literalType;

  public IRClassLiteralBuilder(IRType literalType) {
    _literalType = literalType;
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return new IRClassLiteral( _literalType );
  }
}
