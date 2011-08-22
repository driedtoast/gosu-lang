package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a negation expression, i.e. <code>-foo</code>.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRNegationExpression extends IRExpression {

  private IRExpression _root;

  public IRNegationExpression(IRExpression root) {
    _root = root;
    setParentToThis( root );
  }

  public IRExpression getRoot() {
    return _root;
  }

  @Override
  public IRType getType() {
    return _root.getType();
  }
}
