package gw.lang.ir.builder;

import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRSymbolBuilder {

  private String _name;
  private IRType _type;

  protected IRSymbolBuilder() { }

  public IRSymbolBuilder(String name) {
    _name = name;
  }

  public IRSymbolBuilder(String name, IRType type) {
    _name = name;
    _type = type;
  }

  public IRSymbol build(IRBuilderContext context) {
    if (_type == null) {
      return context.findVar(_name);
    } else {
      return context.getOrCreateVar(_name, _type);
    }
  }
}
