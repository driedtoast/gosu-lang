package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a logical not operation, i.e. <code>!foo</code>.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRNotExpression extends IRExpression {

  private IRExpression _root;

  public IRNotExpression(IRExpression root) {
    _root = root;

    root.setParent( this );
  }

  public IRExpression getRoot() {
    return _root;
  }

  @Override
  public IRType getType() {
    return _root.getType();
  }
}
