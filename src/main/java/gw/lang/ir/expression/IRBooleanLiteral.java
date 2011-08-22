package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a primitive boolean literal, i.e. <code>true</code> or <code>false</code>
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRBooleanLiteral extends IRExpression {
  private boolean _value;

  public IRBooleanLiteral(boolean value) {
    _value = value;
  }

  public boolean getValue() {
    return _value;
  }

  @Override
  public IRType getType() {
    return IRTypeConstants.pBOOLEAN;
  }
}
