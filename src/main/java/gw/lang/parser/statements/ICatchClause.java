package gw.lang.parser.statements;

import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IStatement;
import gw.lang.parser.ISymbol;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ICatchClause extends IStatement, IParsedElementWithAtLeastOneDeclaration
{
  public IType getCatchType();
  public IStatement getCatchStmt();
  public ISymbol getSymbol();
}
