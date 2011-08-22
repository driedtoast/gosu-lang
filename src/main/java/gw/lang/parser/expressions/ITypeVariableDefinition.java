package gw.lang.parser.expressions;

import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITypeVariableDefinition
{
  IType getEnclosingType();
  void setEnclosingType( IType enclosingType );

  String getName();

  IGenericTypeVariable getTypeVar();

  ITypeVariableType getType();

  IType getBoundingType();

  ITypeVariableDefinition clone();
}
