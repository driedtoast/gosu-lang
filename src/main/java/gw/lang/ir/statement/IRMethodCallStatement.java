package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing a method call statement, i.e. <code>foo.bar(baz);</code>.  In the case
 * of a method call statement, the return value (if any) will be popped off the stack at the end.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRMethodCallStatement extends IRStatement {
  private IRExpression _expression;

  public IRMethodCallStatement(IRExpression expression) {
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
