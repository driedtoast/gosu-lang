package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing the release of the monitor lock on an object, as when exiting a
 * synchronize(foo) block.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRMonitorLockReleaseStatement extends IRStatement {
  private IRExpression _monitoredObj;

  public IRMonitorLockReleaseStatement( IRExpression monitoredObject )
  {
    _monitoredObj = monitoredObject;
  }

  public IRExpression getMonitoredObject()
  {
    return _monitoredObj;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement()
  {
    return null;
  }
}