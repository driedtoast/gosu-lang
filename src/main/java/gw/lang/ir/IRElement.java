package gw.lang.ir;

import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public abstract class IRElement {
  private IRElement _parent;
  private int _lineNumber = -1;

  public IRElement getParent() {
    return _parent;
  }

  public void setParent(IRElement parent) {
    _parent = parent;
  }

  protected void setParentToThis( IRElement element ) {
    if (element != null) {
      element.setParent( this );
    }
  }

  public int getLineNumber() {
    return _lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    _lineNumber = lineNumber;
  }
}
