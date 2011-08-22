package gw.lang.reflect;

import gw.lang.parser.expressions.ITypeVariableDefinition;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITypeVariableType extends INonLoadableType
{
  ITypeVariableDefinition getTypeVarDef();

  String getNameWithEnclosingType();

  IType getBoundingType();

  boolean isFunctionStatement();
}
