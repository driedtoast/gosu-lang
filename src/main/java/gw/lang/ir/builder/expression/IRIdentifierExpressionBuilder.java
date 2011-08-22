package gw.lang.ir.builder.expression;

import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRSymbolBuilder;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRIdentifier;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRIdentifierExpressionBuilder extends IRExpressionBuilder {

  private IRSymbolBuilder _symbol;

  public IRIdentifierExpressionBuilder(IRSymbolBuilder symbol) {
    _symbol = symbol;
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return new IRIdentifier(_symbol.build(context));
  }
}
