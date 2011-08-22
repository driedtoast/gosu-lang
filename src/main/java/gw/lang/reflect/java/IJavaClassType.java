package gw.lang.reflect.java;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IType;

import java.io.Serializable;
import java.util.Map;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaClassType extends Serializable
{
  IType getActualType( TypeVarToTypeMap typeMap );
  IType getActualType( TypeVarToTypeMap typeMap, boolean bKeepTypeVars );
  String getName();
}