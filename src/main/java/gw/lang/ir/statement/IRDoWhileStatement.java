package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing a do/while loop, such as:
 * <code>
 * do {
 *  . . .
 * } while (condition)
 * </code>
 *
 * A do/while statement consists of a body statement (which may itself be a statement list) and a test expression
 * which must evaluate to a <code>boolean</code> value.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRDoWhileStatement extends IRStatement implements IRLoopStatement {

  // test
  private IRExpression _test;

  // body
  private IRStatement _body;

  public IRExpression getLoopTest()
  {
    return _test;
  }

  public void setLoopTest( IRExpression test )
  {
    _test = test;
    setParentToThis( test );
  }

  public IRStatement getBody()
  {
    return _body;
  }

  public void setBody( IRStatement irStatement )
  {
    _body = irStatement;
    setParentToThis( irStatement );
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    // do/while loops always execute at least once, so we can look for a terminal statement
    // within the body of the loop
    if (_body != null) {
      IRTerminalStatement terminalStmt = _body.getLeastSignificantTerminalStatement();
      if( terminalStmt instanceof IRReturnStatement ||
          terminalStmt instanceof IRThrowStatement )
      {
        return terminalStmt;
      }
    }
    return null;
  }
}