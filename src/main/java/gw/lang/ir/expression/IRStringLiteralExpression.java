package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a String literal expression such as <code>"foo"</code>.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRStringLiteralExpression extends IRExpression {
  private String _value;

  public IRStringLiteralExpression(String value) {
    _value = value;
  }

  public String getValue() {
    return _value;
  }

  @Override
  public IRType getType() {
    return IRTypeConstants.STRING;
  }
}
