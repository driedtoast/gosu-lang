package gw.lang.debugger;

import gw.lang.parser.IActivationContext;

import java.util.List;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IDebugService
{
  /**
   * If specified the UserId limits the scope of the debugger to calls
   * corresponding to the user's session. If null, any session is fair game.
   */
  public String getUserName();
  public void setUserName( String strUserName );

  /**
   * Call this to do a run, step over (also used for intercept in studio), or step into.
   *
   * WARNING:  This method hangs until "something" happens.  I.e. a breakpoint, the program finishes, etc...
   *           For step over or step into it should always return "immediately". i.e. on the next executable line.
   * @param iCmd
   * @return
   */
  public DebugLocationContext debug( IDebugDriver.DebugCommands iCmd, List<BreakPoint> breakpoints );

  /**
   * Stops the debugger
   */
  public void stopDebugging();

  /**
   * Adds a breakpoint
   * @param breakPoint
   */
  public void addBreakPoint( BreakPoint breakPoint );

  /**
   * Removes a breakpoint
   * @param breakPoint
   */
  public void removeBreakPoint( BreakPoint breakPoint );

  public boolean areBreakpointsMuted();
  public void setBreakpointsMuted( boolean bMuted );

  /**
   * Returns the current execution stacks.  Each element in the array is a stack frame.
   *
   * @return
   */
  public IActivationContext[] getActivationContextStack();

  /**
   * Call this method once a break point is hit to establish the symbols for the specified stack frame.
   */
  public DebugExpression[] establishSymbols( int iScopeIndex, int iPrivateGlobalScopeIndex );
  public DebugExpression[] getEstablishedSymbols();

  /**
   * Evaluates the given expressions relative to a call to establish symbols above.
   * @param astrExpressions
   */
  public DebugExpression[] evaluate( String[] astrExpressions );

  // Next two methods are only for the gosu tester.
  public String[] evaluate( String strScript );
  public String executeTemplate( String strTemplate );

}
