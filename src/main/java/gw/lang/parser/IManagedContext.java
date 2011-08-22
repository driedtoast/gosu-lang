package gw.lang.parser;

import gw.lang.debugger.IDebugContextProperties;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IManagedContext extends IRuntimeObserver
{
  public IDebugContextProperties getContext();

  public boolean isDebugging();

  /**
   * A human readable name for this context. For instance, the managed context
   * for a Gosu class might be the class name.
   */
  public String getDisplayName();

  void setParentManagedContext( IManagedContext parentManagedContext );
}
