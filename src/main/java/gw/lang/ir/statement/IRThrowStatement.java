package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing a throw statement, such as <code>throw new RuntimeException()</code>.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRThrowStatement extends IRStatement implements IRTerminalStatement {
  private IRExpression _exception;

  public IRThrowStatement(IRExpression exception) {
    _exception = exception;
    exception.setParent( this );
  }

  public IRExpression getException() {
    return _exception;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return this;
  }
}
