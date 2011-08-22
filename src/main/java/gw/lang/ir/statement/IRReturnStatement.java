package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing a return statement, i.e. <code>return foo</code>.  The return statement
 * may or may not have an associated returnValue expression.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRReturnStatement extends IRStatement implements IRTerminalStatement {
  private IRExpression _returnValue;
  private IRStatement _tempVarAssignment;

  public IRReturnStatement() {
  }
  
  public IRReturnStatement(IRStatement tempVarAssignment, IRExpression returnValue) {
    _tempVarAssignment = tempVarAssignment;
    _returnValue = returnValue;
    if (returnValue != null) {
      returnValue.setParent( this );
    }
  }

  public IRExpression getReturnValue() {
    return _returnValue;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return this;
  }

  public boolean hasTempVar()
  {
    return _tempVarAssignment != null;
  }

  public IRStatement getTempVarAssignment()
  {
    return _tempVarAssignment;
  }
}
