package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a new array expression, i.e. <code>new String[10]</code>.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRNewArrayExpression extends IRExpression {
  private IRType _componentType;
  private IRExpression _sizeExpression;

  public IRNewArrayExpression(IRType componentType, IRExpression sizeExpression) {
    _componentType = componentType;
    _sizeExpression = sizeExpression;

    sizeExpression.setParent( this );
  }

  public IRType getComponentType() {
    return _componentType;
  }

  public IRType getType() {
    return _componentType.getArrayType();
  }

  public IRExpression getSizeExpression() {
    return _sizeExpression;
  }
}
