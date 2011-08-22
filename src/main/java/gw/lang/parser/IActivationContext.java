package gw.lang.parser;

import gw.lang.debugger.DebugLocationContext;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IActivationContext
{
  Object getContext();

  DebugLocationContext getDebugContext();

  void setDebugContext( DebugLocationContext debugCtx );

  int getCurrentScopeIndex();

  void setCurrentScopeIndex( int iIndex );

  int getCurrentPrivateGlobalScopeIndex();

  void setCurrentPrivateGlobalScopeIndex( int iIndex );

  String getLabel();

  boolean isTransparent();
}
