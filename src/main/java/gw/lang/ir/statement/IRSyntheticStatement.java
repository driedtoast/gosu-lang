package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing a wrapped expression whose value should be discarded.  This statment type is generally
 * used in cases where evaluation of an expression is necessitated in order to force any side effects to happen
 * but where the value of the expression is not actually needed in the computation.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRSyntheticStatement extends IRStatement {
  private IRExpression _expression;

  public IRSyntheticStatement(IRExpression expression) {
    _expression = expression;
    expression.setParent( this );
  }

  public IRExpression getExpression() {
    return _expression;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return null;
  }
}
