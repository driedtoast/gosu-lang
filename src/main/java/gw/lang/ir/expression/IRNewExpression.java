package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

import java.util.List;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a new expression, i.e. <code>new Foo(arg1, arg2)</code>.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRNewExpression extends IRExpression {
  private IRType _ownersType;
  private List<IRType> _parameterTypes;
  private List<IRExpression> _args;

  public IRNewExpression(IRType ownersType, List<IRType> parameterTypes, List<IRExpression> args) {
    _ownersType = ownersType;
    _parameterTypes = parameterTypes;
    _args = args;

    for (IRExpression arg : args) {
      arg.setParent( this );
    }
  }

  public IRType getOwnersType() {
    return _ownersType;
  }

  public List<IRType> getParameterTypes() {
    return _parameterTypes;
  }

  public List<IRExpression> getArgs() {
    return _args;
  }

  @Override
  public IRType getType() {
    return _ownersType;
  }
}
