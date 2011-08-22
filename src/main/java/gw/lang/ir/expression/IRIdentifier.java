package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRSymbol;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing an identifier, i.e. <code>foo</code>.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRIdentifier extends IRExpression {
  private IRSymbol _symbol;

  public IRIdentifier(IRSymbol symbol) {
    if (symbol == null) {
      throw new IllegalArgumentException("symbol argument cannot be null");
    }
    _symbol = symbol;
  }

  public IRSymbol getSymbol() {
    return _symbol;
  }

  @Override
  public IRType getType() {
    return _symbol.getType();
  }
}
