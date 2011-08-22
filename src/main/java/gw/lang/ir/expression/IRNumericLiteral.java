package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a primitive numeric literal, i.e. <code>5</code> or <code>12.123</code>.
 * The literal in question could represent either a byte, short, int, long, float, or double.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRNumericLiteral extends IRExpression {
  private Number _value;

  public IRNumericLiteral(Number value) {
    _value = value;
  }

  public Number getValue() {
    return _value;
  }

  @Override
  public IRType getType() {
    if (_value instanceof Byte) {
      return IRTypeConstants.pBYTE;
    } else if (_value instanceof Short) {
      return IRTypeConstants.pSHORT;
    } else if (_value instanceof Integer) {
      return IRTypeConstants.pINT;
    } else if (_value instanceof Long) {
      return IRTypeConstants.pLONG;
    } else if (_value instanceof Float) {
      return IRTypeConstants.pFLOAT;
    } else if (_value instanceof Double) {
      return IRTypeConstants.pDOUBLE;
    } else {
      throw new IllegalStateException("Unexpected value " + _value.getClass());
    }
  }
}
