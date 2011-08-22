package gw.lang.ir.builder;

import gw.lang.ir.IRType;
import gw.lang.ir.IRSymbol;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRTempSymbolBuilder extends IRSymbolBuilder {

  private IRType _type;
  private IRSymbol _symbol;

  public IRTempSymbolBuilder(IRType type) {
    _type = type;
  }

  @Override
  public IRSymbol build(IRBuilderContext context) {
    if (_symbol == null) {
      _symbol = context.tempSymbol(_type);
    }
    return _symbol;
  }
}
