package gw.lang.debugger;

import gw.lang.parser.IActivationContext;

import java.io.Serializable;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class RemoteActivationContext implements IActivationContext, Serializable {
  private DebugLocationContext _debugCtx;
  private int _iCurrentScopeIndex;
  private int _iCurrentPrivateGlobalScopeIndex;
  private String _strLabel;
  private boolean _bTransparent;

  public RemoteActivationContext( DebugLocationContext debugCtx, int iCurrentScopeIndex, String strLabel )
  {
    _debugCtx = debugCtx;
    _iCurrentScopeIndex = iCurrentScopeIndex;
    _bTransparent = true;
    makeLabel( strLabel );
  }

  public Object getContext() {
    return null;
  }

  public DebugLocationContext getDebugContext() {
    return _debugCtx;
  }

  public void setDebugContext(DebugLocationContext debugCtx) {
    _debugCtx = debugCtx;
  }

  public int getCurrentScopeIndex() {
    return _iCurrentScopeIndex;
  }

  public void setCurrentScopeIndex(int iIndex) {
    _iCurrentScopeIndex = iIndex;
  }

  public int getCurrentPrivateGlobalScopeIndex() {
    return _iCurrentPrivateGlobalScopeIndex;
  }

  public void setCurrentPrivateGlobalScopeIndex(int iIndex) {
    _iCurrentPrivateGlobalScopeIndex = iIndex;
  }

  public String getLabel() {
    return _strLabel;
  }

  public boolean isTransparent() {
    return _bTransparent;
  }

  public String toString() {
    return _strLabel;
  }

  private void makeLabel(String strLabel) {
    if (_debugCtx == null) {
      _strLabel = strLabel;
      return;
    }

    IDebugContextProperties context = _debugCtx.getContext();
    _strLabel = context == null ? strLabel : context.getFormattedStackTraceTitle(strLabel, _debugCtx);
  }
}
