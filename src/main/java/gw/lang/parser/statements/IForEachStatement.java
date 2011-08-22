package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IStatement;
import gw.lang.parser.ISymbol;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IForEachStatement extends ILoopStatement, IParsedElementWithAtLeastOneDeclaration
{
  ISymbol getIdentifier();

  ISymbol getIndexIdentifier();

  IExpression getInExpression();

  IStatement getStatement();
}
