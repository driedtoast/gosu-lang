package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing the break operation, i.e. <code>break</code>.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRBreakStatement extends IRStatement implements IRTerminalStatement {
  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return this;
  }
}