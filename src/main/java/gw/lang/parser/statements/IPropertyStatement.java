package gw.lang.parser.statements;

import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IStatement;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IPropertyStatement extends IStatement, IParsedElementWithAtLeastOneDeclaration
{
  IFunctionStatement getPropertyGetterOrSetter();

  IDynamicPropertySymbol getDps();
}
