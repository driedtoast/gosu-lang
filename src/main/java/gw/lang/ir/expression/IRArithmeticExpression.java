package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a primitive arithmetic, shift, or bitwise operation, i.e. <code>x + y</code>
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRArithmeticExpression extends IRExpression {

  /**
   * The operation being performed.
   */
  public static enum Operation {
    Addition, Subtraction, Multiplication, Division, Remainder, ShiftLeft, ShiftRight, UnsignedShiftRight, BitwiseAnd, BitwiseOr, BitwiseXor
  }

  private IRType _type;
  private IRExpression _lhs;
  private IRExpression _rhs;
  private Operation _op;

  public IRArithmeticExpression(IRType type, IRExpression lhs, IRExpression rhs, Operation op) {
    _type = type;
    _lhs = lhs;
    _rhs = rhs;
    _op = op;

    lhs.setParent( this );
    rhs.setParent( this );
  }

  public IRType getType() {
    return _type;
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
}
