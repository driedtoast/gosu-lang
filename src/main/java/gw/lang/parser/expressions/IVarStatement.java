package gw.lang.parser.expressions;

import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.GlobalScope;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.IStatement;
import gw.lang.parser.ISymbol;
import gw.lang.reflect.IModifierInfo;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IVarStatement extends IStatement, IParsedElementWithAtLeastOneDeclaration
{
  CaseInsensitiveCharSequence getIdentifierName();

  ISymbol getSymbol();

  CaseInsensitiveCharSequence getPropertyName();

  GlobalScope getScope();

  ITypeLiteralExpression getTypeLiteral();

  IExpression getAsExpression();

  boolean hasProperty();

  IModifierInfo getModifierInfo();

  int getModifiers();

  boolean isStatic();

  boolean isPrivate();

  boolean isInternal();

  boolean isProtected();

  boolean isPublic();

  boolean isFinal();

  boolean isEnumConstant();

  IType getType();

  IScriptPartId getScriptPart();

  boolean getHasInitializer();

  String getFullDescription();

  void setSymbol( ISymbol symbol );
  
  public int getPropertyNameOffset();

  boolean isFieldDeclaration();
}
