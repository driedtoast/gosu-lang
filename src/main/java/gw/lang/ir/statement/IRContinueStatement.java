package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing a continue statement, i.e. <code>continue</code>.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRContinueStatement extends IRStatement implements IRTerminalStatement {

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return this;
  }
}