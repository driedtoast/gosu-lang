package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a field get, i.e. <code>foo.bar</code>.  If the left-hand side
 * expression is null, that means this is a static field get.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRFieldGetExpression extends IRExpression {
  private IRExpression _lhs;
  private String _name;
  private IRType _fieldType;
  private IRType _ownersType;

  public IRFieldGetExpression(IRExpression lhs, String name, IRType fieldType, IRType ownersType) {
    _lhs = lhs;
    _name = name;
    _fieldType = fieldType;
    _ownersType = ownersType;

    if (lhs != null) {
      lhs.setParent( this );
    }
  }

  public IRExpression getLhs() {
    return _lhs;
  }

  public String getName() {
    return _name;
  }

  public IRType getFieldType() {
    return _fieldType;
  }

  public IRType getOwnersType() {
    return _ownersType;
  }

  @Override
  public IRType getType() {
    return _fieldType;
  }
}