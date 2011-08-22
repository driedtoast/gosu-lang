package gw.lang.parser;

import gw.util.CaseInsensitiveHashMap;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ExternalSymbolMapForMap extends ExternalSymbolMapBase {

  private CaseInsensitiveHashMap<CaseInsensitiveCharSequence, ISymbol> _externalSymbols;

  public ExternalSymbolMapForMap(CaseInsensitiveHashMap<CaseInsensitiveCharSequence, ISymbol> externalSymbols) {
    this(externalSymbols, false);
  }

  public ExternalSymbolMapForMap(CaseInsensitiveHashMap<CaseInsensitiveCharSequence, ISymbol> externalSymbols, boolean assumeSymbolsRequireExternalSymbolMapArgument) {
    super(assumeSymbolsRequireExternalSymbolMapArgument);
    _externalSymbols = externalSymbols;
  }

  public ISymbol getSymbol(String name) {
    return _externalSymbols.get(CaseInsensitiveCharSequence.get(name));
  }

  public boolean isExternalSymbol(String name) {
    return _externalSymbols.containsKey(CaseInsensitiveCharSequence.get(name));
  }
}
