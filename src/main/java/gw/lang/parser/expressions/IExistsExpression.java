package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.ISymbol;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IExistsExpression extends IExpression, IParsedElementWithAtLeastOneDeclaration
{
  ISymbol getIdentifier();

  ISymbol getIndexIdentifier();

  IExpression getInExpression();

  IExpression getWhereExpression();
}
