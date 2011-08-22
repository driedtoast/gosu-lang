package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a ternary expression such as <code>foo ? bar : baz</code>.  The test
 * expression must be of type <code>boolean</code>, and both trueValue and falseValue must be assignable
 * to resultType.  The resultType must be explicitly specified because it's difficult to infer from just
 * trueValue and falseValue:  generally it should be the least-upper-bound of the types of the two expressions,
 * but it's important for compilation purposes that it be set to whatever the language infers the resulting type
 * to be so that the necessary implicit casts/boxing/unboxing can be added in.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRTernaryExpression extends IRExpression {
  private IRExpression _test;
  private IRExpression _trueValue;
  private IRExpression _falseValue;
  private IRType _resultType;

  public IRTernaryExpression(IRExpression test, IRExpression trueValue, IRExpression falseValue, IRType resultType) {
    _test = test;
    _trueValue = trueValue;
    _falseValue = falseValue;
    _resultType = resultType;

    test.setParent( this );
    trueValue.setParent( this );
    falseValue.setParent( this );
  }

  public IRExpression getTest() {
    return _test;
  }

  public IRExpression getTrueValue() {
    return _trueValue;
  }

  public IRExpression getFalseValue() {
    return _falseValue;
  }

  public IRType getResultType() {
    return _resultType;
  }

  @Override
  public IRType getType() {
    return _resultType;
  }
}
