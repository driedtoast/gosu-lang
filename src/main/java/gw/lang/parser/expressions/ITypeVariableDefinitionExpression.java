package gw.lang.parser.expressions;

import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITypeVariableDefinitionExpression extends IParsedElementWithAtLeastOneDeclaration
{
  public ITypeVariableDefinition getTypeVarDef();  
}
