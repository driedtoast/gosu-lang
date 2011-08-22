package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing the built-in array length property, i.e. <code>foo.length</code>
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRArrayLengthExpression extends IRExpression {
  private IRExpression _root;

  public IRArrayLengthExpression(IRExpression root) {
    _root = root;
    root.setParent( this );
  }

  public IRExpression getRoot() {
    return _root;
  }

  @Override
  public IRType getType() {
    return IRTypeConstants.pINT;
  }
}
