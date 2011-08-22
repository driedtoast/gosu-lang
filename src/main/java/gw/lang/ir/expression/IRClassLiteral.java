package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.GosuShop;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a Class literal, i.e. <code>String.class</code>
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRClassLiteral extends IRExpression {

  private IRType _literalType;

  public IRClassLiteral(IRType literalType) {
    _literalType = literalType;
  }

  public IRType getLiteralType() {
    return _literalType;
  }

  @Override
  public IRType getType() {
    return GosuShop.getIRTypeResolver().getDescriptor( Class.class );
  }
}
