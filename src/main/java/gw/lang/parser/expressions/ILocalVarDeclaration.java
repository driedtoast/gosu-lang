package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.ISymbol;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ILocalVarDeclaration extends IExpression, IParsedElementWithAtLeastOneDeclaration
{
  CharSequence getLocalVarName();

  ITypeLiteralExpression getTypeLiteral();

  ISymbol getSymbol();
}
