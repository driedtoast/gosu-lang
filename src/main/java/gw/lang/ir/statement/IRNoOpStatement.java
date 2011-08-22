package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing a no-op.  Generally used as a placeholder in cases where at least one
 * statement is expected, such as in the body of an if statement.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRNoOpStatement extends IRStatement {
  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return null;
  }
}
