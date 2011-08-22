package gw.lang.debugger;

import java.io.Serializable;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IDebugContextProperties extends Serializable
{
  public Serializable getContextProperty( String strProperty );

  public String getFormattedStackTraceTitle( String strContext, DebugLocationContext location );

  public String getBreakpointTitle();

  public String getEnclosingType();

  /**
   * Does this debug context have source code to debug? This concept facilitates
   * the case where you want to have a breakpoint on an empty context e.g.,
   * break on a rule set with not rules defined -- you still want to be able to
   * step into a rule set even though there are no rules.
   */
  public boolean hasSource();

  /**
   * @return true if the code associatd with this context is debuggable.
   *   A proxy gosu class's code is an example of non-debuggable code.
   */
  boolean isDebuggable();
}
