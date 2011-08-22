package gw.lang.debugger;

import gw.lang.parser.IActivationContext;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class AbstractActivationContext implements IActivationContext
{
  private Object _ctx;
  private int _iCurrentScopeIndex;
  private int _iCurrentPrivateGlobalScopeIndex;
  private DebugLocationContext _debugCtx;

  public AbstractActivationContext( Object ctx )
  {
    _ctx = ctx;
  }

  public Object getContext()
  {
    return _ctx;
  }

  public DebugLocationContext getDebugContext()
  {
    return _debugCtx;
  }

  public void setDebugContext( DebugLocationContext debugCtx )
  {
    _debugCtx = debugCtx;
  }

  public int getCurrentScopeIndex()
  {
    return _iCurrentScopeIndex;
  }

  public void setCurrentScopeIndex( int iIndex )
  {
    _iCurrentScopeIndex = iIndex;
  }

  public int getCurrentPrivateGlobalScopeIndex()
  {
    return _iCurrentPrivateGlobalScopeIndex;
  }

  public void setCurrentPrivateGlobalScopeIndex( int iIndex )
  {
    _iCurrentPrivateGlobalScopeIndex = iIndex;
  }

  public boolean isTransparent()
  {
    return false;
  }
}
