package gw.lang.ir.builder;

import gw.lang.ir.IRSymbol;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRThisSymbolBuilder extends IRSymbolBuilder {

  @Override
  public IRSymbol build(IRBuilderContext context) {
    return new IRSymbol("this", context.owningType(), false);
  }
}
