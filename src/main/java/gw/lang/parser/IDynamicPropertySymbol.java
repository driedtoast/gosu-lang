package gw.lang.parser;

import gw.lang.reflect.gs.IGosuClass;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IDynamicPropertySymbol extends IDynamicSymbol
{
  boolean isReadable();

  IDynamicFunctionSymbol getGetterDfs();

  IDynamicFunctionSymbol getSetterDfs();

  IDynamicPropertySymbol getParent();

  IDynamicFunctionSymbol getFunction( CaseInsensitiveCharSequence strFunctionName );

  CaseInsensitiveCharSequence getVarIdentifier();

  String getFullDescription();

  IDynamicPropertySymbol getParameterizedVersion( IGosuClass gsClass );

  boolean isStatic();

  void setGetterDfs( IDynamicFunctionSymbol dfsGetter );

  void setSetterDfs( IDynamicFunctionSymbol dfsSetter );
}
