package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a relational expression such as <code>foo > 10</code>.  The operation
 * in question could be one of >, >=, <, or <=.  Both operands to this expression must be primitive
 * numeric types, and they must both be of the same type.  Relational expressions always result
 * in a primitive boolean value.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRRelationalExpression extends IRExpression {

  private IRExpression _lhs;
  private IRExpression _rhs;
  private Operation _op;

  public IRRelationalExpression(IRExpression lhs, IRExpression rhs, Operation op) {
    _lhs = lhs;
    _rhs = rhs;
    _op = op;

    lhs.setParent( this );
    rhs.setParent( this );
  }

  public enum Operation {
    GT, GTE, LT, LTE
  }

  public IRExpression getLhs() {
    return _lhs;
  }

  public IRExpression getRhs() {
    return _rhs;
  }

  public Operation getOp() {
    return _op;
  }

  @Override
  public IRType getType() {
    return IRTypeConstants.pBOOLEAN;
  }
}
