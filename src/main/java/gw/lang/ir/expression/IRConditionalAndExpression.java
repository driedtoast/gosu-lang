package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a conditional AND expression, i.e. <code>foo && bar</code>
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRConditionalAndExpression extends IRExpression {
  private IRExpression _lhs;
  private IRExpression _rhs;

  public IRConditionalAndExpression(IRExpression lhs, IRExpression rhs) {
    _lhs = lhs;
    _rhs = rhs;

    _lhs.setParent( this );
    _rhs.setParent( this );
  }

  public IRExpression getLhs() {
    return _lhs;
  }

  public IRExpression getRhs() {
    return _rhs;
  }

  @Override
  public IRType getType() {
    return IRTypeConstants.pBOOLEAN;
  }
}
