package gw.lang.reflect.gs;

import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGenericTypeVariable
{
  String getName();

  String getNameWithBounds( boolean bRelative );

  ITypeVariableDefinition getTypeVariableDefinition();

  IType getBoundingType();

  IGenericTypeVariable clone();

  void createTypeVariableDefinition(IType enclosingType);
}
