package gw.lang.parser;

import gw.lang.reflect.gs.IGosuClass;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IDynamicSymbol extends IFunctionSymbol
{
  IScriptPartId getScriptPart();
  IGosuClass getGosuClass();
}
