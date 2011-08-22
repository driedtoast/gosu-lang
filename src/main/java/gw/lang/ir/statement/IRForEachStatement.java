package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRIdentifier;
import gw.lang.UnstableAPI;

import java.util.List;
import java.util.ArrayList;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing an foreach statement, such as
 * <code>
 * for (int i = 0; i < array.length; i++) {
 *   . . .
 * }
 * </code>
 *
 * There are many ways that a higher-level for statement in a language can compile down to an IRForEachStatement.
 * For example, the for statement could be iterating over an array using a loop index or it could be iterating
 * over an Iterator object.  Those higher-level constructs always break down into a combination of initializers,
 * which are always executed prior to the loop (such as <code>int i = 0</code>), the test expression that's executed
 * prior to each execution of the loop (such as <code>i < array.length</code>), increment expressions that are executed
 * at the end of each iteration (such as <code>i++</code>), and the body of the loop.  In addition, there's an optional
 * identifier to null-check prior to the first execution of the loop.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRForEachStatement extends IRStatement implements IRLoopStatement {

  // init
  private List<IRStatement> _initializers = new ArrayList<IRStatement>();

  // test
  private IRExpression _test;

  // increment
  private List<IRStatement> _incrementors = new ArrayList<IRStatement>();

  // body
  private IRStatement _body;

  // identifier, if any, to null check
  private IRIdentifier _identifierToNullCheck;

  public List<IRStatement> getInitializers()
  {
    return _initializers;
  }

  public void addInitializer( IRStatement initializer )
  {
    _initializers.add( initializer );
    setParentToThis( initializer );
  }

  public List<IRStatement> getIncrementors()
  {
    return _incrementors;
  }

  public void addIncrementor( IRStatement incrementor )
  {
    _incrementors.add( incrementor );
    setParentToThis( incrementor );
  }

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

  public boolean hasIdentifierToNullCheck()
  {
    return _identifierToNullCheck != null;
  }

  public IRIdentifier getIdentifierToNullCheck()
  {
    return _identifierToNullCheck;
  }

  public void setIdentifierToNullCheck( IRIdentifier exprToNullCheck )
  {
    _identifierToNullCheck = exprToNullCheck;
    setParentToThis( exprToNullCheck );
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return null;
  }
}