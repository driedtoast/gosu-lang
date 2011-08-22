package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing an instanceof expression, i.e. <code>foo instanceof String</code>
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRInstanceOfExpression extends IRExpression {
  private IRExpression _root;
  private IRType _testType;

  public IRInstanceOfExpression(IRExpression root, IRType testType) {
    _root = root;
    _testType = testType;

    root.setParent( this );
  }

  public IRExpression getRoot() {
    return _root;
  }

  public IRType getTestType() {
    return _testType;
  }

  public IRType getType() {
    return IRTypeConstants.pBOOLEAN;
  }


}
