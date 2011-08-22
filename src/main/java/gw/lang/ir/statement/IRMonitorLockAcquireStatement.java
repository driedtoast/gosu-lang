package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing the acquisition of the monitor lock on an object, as when entering a
 * synchronize(foo) block.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRMonitorLockAcquireStatement extends IRStatement {
  private IRExpression _monitoredObj;

  public IRMonitorLockAcquireStatement( IRExpression monitoredObject )
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