package gw.lang.parser.statements;

import gw.lang.parser.IDynamicFunctionSymbol;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ISyntheticFunctionStatement extends INoOpStatement
{
  IDynamicFunctionSymbol getDfsOwner();
}
