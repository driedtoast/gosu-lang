package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing an equality or inequality expression, i.e. <code>foo == bar</code> or
 * <code>foo != bar</code>
 *
 * Both the left-hand side and right-hand side expressions must either be the same type of primitive or
 * must both be object types.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IREqualityExpression extends IRExpression {
  private IRExpression _lhs;
  private IRExpression _rhs;
  private boolean _equals;

  public IREqualityExpression(IRExpression lhs, IRExpression rhs, boolean equals) {
    if (lhs.getType().isPrimitive()) {
      if (!lhs.getType().equals(rhs.getType())) {
        throw new IllegalArgumentException("Cannot build an equality expression between a primitive type and something other than the same type. LHS type is " + lhs.getType().getName() + " and RHS type is " + rhs.getType().getName());
      }
    } else if (rhs.getType().isPrimitive()) {
      throw new IllegalArgumentException("Cannot build an equality expression between an object and a primitive type. LHS type is " + lhs.getType().getName() + " and RHS type is " + rhs.getType().getName());
    }

    _lhs = lhs;
    _rhs = rhs;
    _equals = equals;

    _lhs.setParent( this );
    _rhs.setParent( this );
  }

  public IRExpression getLhs() {
    return _lhs;
  }

  public IRExpression getRhs() {
    return _rhs;
  }

  public boolean isEquals() {
    return _equals;
  }

  @Override
  public IRType getType() {
    return IRTypeConstants.pBOOLEAN;
  }
}
