package gw.lang.debugger;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IDebugDriver
{
  public enum DebugCommands
  {
    CMD_RUN,
    CMD_STEP_OVER,
    CMD_STEP_INTO
  }

  public void onLocation( DebugLocationContext context );

  public Iterable getCallStack();

  public IDebugDriver.DebugCommands getCommand();
}
