package gw.lang.debugger;

import gw.lang.parser.RuntimeInfoAtStatement;
import gw.lang.parser.IRuntimeObserver;

import java.util.List;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IDebugManager extends IRuntimeObserver
{
  public boolean isDebugging();
  public void setDebugging( boolean bDebugging, IDebugDriver driver );

  public void addBreakPoint( BreakPoint bp );
  public void removeBreakPoint( BreakPoint bp );
  public List getBreakPoints();

  public void onBeforeExecute( RuntimeInfoAtStatement ctx );
}
