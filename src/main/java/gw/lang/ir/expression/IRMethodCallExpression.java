package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

import java.util.List;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a method call, i.e. <code>foo.bar(baz)</code>.  If the root expression
 * is null, that means this is a static method call.  Flags on this class also indicate whether or not
 * this invocation should be compiled using INVOKEINTERFACE or INVOKESPECIAL rather than INVOKEVIRTUAL
 * in the case of an instance method invocation.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRMethodCallExpression extends IRExpression {
  private String _name;
  private IRType _ownersType;
  private boolean _interface;
  private IRType _returnType;
  private List<IRType> _parameterTypes;
  private IRExpression _root;
  private List<IRExpression> _args;
  private boolean _isSpecial;

  public IRMethodCallExpression(String name, IRType ownersType, boolean isInterface, IRType returnType, List<IRType> parameterTypes, IRExpression root, List<IRExpression> args) {
    _name = name;
    _ownersType = ownersType;
    _interface = isInterface;
    _returnType = returnType;
    _parameterTypes = parameterTypes;
    _root = root;
    _args = args;

    if (root != null) {
      root.setParent( this );
    }
    for (IRExpression arg : args) {
      arg.setParent( this );
    }
  }

  public String getName() {
    return _name;
  }

  public IRType getOwnersType() {
    return _ownersType;
  }

  public IRType getReturnType() {
    return _returnType;
  }

  public List<IRType> getParameterTypes() {
    return _parameterTypes;
  }

  public IRExpression getRoot() {
    return _root;
  }

  public List<IRExpression> getArgs() {
    return _args;
  }

  public boolean isInterface() {
    return _interface;
  }

  public boolean isSpecial() {
    return _isSpecial;
  }

  public void setSpecial(boolean special) {
    _isSpecial = special;
  }

  @Override
  public IRType getType() {
    return _returnType;
  }
}
