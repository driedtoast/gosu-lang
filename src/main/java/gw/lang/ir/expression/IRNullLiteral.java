package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing the null literal, i.e. <code>null</code>.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRNullLiteral extends IRExpression {

  public IRNullLiteral() { }

  @Override
  public IRType getType() {
    // This is Object instead of void because we need to be clear that A) this isn't primitive and B) it implies a value on
    // the stack, as opposed to void, which is primitive and implies the absence of anything on the stack
    return IRTypeConstants.OBJECT;
  }
}
