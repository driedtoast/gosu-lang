package gw.lang.parser.statements;

import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IStatement;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IFunctionStatement extends IStatement, IParsedElementWithAtLeastOneDeclaration
{
  IDynamicFunctionSymbol getDynamicFunctionSymbol();
}
