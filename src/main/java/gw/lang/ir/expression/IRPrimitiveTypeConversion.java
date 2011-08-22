package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a primitive type conversion operation, i.e. <code>(double) foo</code>.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRPrimitiveTypeConversion extends IRExpression {
  private IRExpression _root;
  private IRType _fromType;
  private IRType _toType;

  public IRPrimitiveTypeConversion(IRExpression root, IRType fromType, IRType toType) {
    _root = root;
    _fromType = fromType;
    _toType = toType;

    root.setParent( this );
  }

  public IRExpression getRoot() {
    return _root;
  }

  public IRType getFromType() {
    return _fromType;
  }

  public IRType getToType() {
    return _toType;
  }

  @Override
  public IRType getType() {
    return _toType;
  }
}
