package gw.lang.ir.statement;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;


/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing an eval statement, such as <code>eval(someString)</code>.  The argument
 * to the eval statement must evaluate to a <code>String</code>.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IREvalStatement extends IRStatement
{
  private IRExpression _expression;

  public IREvalStatement( IRExpression expression )
  {
    _expression = expression;
    expression.setParent( this );
  }

  public IRExpression getExpression()
  {
    return _expression;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement()
  {
    return null;
  }
}