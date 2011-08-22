package gw.lang.ir.builder.statement;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.builder.IRArgConverter;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRStatementBuilder;
import gw.lang.ir.statement.IRReturnStatement;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRReturnStatementBuilder extends IRStatementBuilder {

  private IRExpressionBuilder _value;

  public IRReturnStatementBuilder() {
  }

  public IRReturnStatementBuilder(IRExpressionBuilder value) {
    _value = value;
  }

  @Override
  protected IRStatement buildImpl(IRBuilderContext context) {
    IRExpression value;
    if (_value == null) {
      value = null;
    } else {
      value = IRArgConverter.castOrConvertIfNecessary( context.currentReturnType(), _value.build( context ) );
    }
    return new IRReturnStatement(null, value);
  }
}
