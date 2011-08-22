package gw.lang.ir;

import gw.lang.ir.statement.IRTerminalStatement;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public abstract class IRStatement extends IRElement {
  private String _originalSourceStatement = null;

  public String getOriginalSourceStatement() {
    return _originalSourceStatement;
  }

  public void setOriginalSourceStatement(String originalSourceStatement) {
    _originalSourceStatement = originalSourceStatement;
  }

  public abstract IRTerminalStatement getLeastSignificantTerminalStatement();
}
