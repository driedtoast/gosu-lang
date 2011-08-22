package gw.lang.parser;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ExternalSymbolMapSymbolTableWrapper extends ExternalSymbolMapBase {
  private ISymbolTable _table;

  public ExternalSymbolMapSymbolTableWrapper(ISymbolTable table) {
    this(table, false);
  }

  public ExternalSymbolMapSymbolTableWrapper(ISymbolTable table, boolean assumeSymbolsRequireExternalSymbolMapArgument) {
    super(assumeSymbolsRequireExternalSymbolMapArgument);
    _table = table;
  }

  public ISymbol getSymbol(String name) {
    return _table.getSymbol(name);
  }
}
