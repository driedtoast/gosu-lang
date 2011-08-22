package gw.lang.ir.statement;

import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing a catch clause within a try/catch/finally statement, such as:
 *
 * <code>
 * try {
 *   . . .
 * } catch (Throwable t) {
 *   . . .
 * }
 * </code>
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRCatchClause {
  private IRSymbol _identifier;
  private IRStatement _body;

  public IRCatchClause(IRSymbol identifier, IRStatement body) {
    _identifier = identifier;
    _body = body;
    // Note that the body of the catch clause gets its parent set to the enclosing
    // IRTryCatchFinallyStatement, rather than the catch clause, which isn't itself really a statement
  }

  public IRSymbol getIdentifier() {
    return _identifier;
  }

  public IRStatement getBody() {
    return _body;
  }
}
