package gw.lang.parser;

import gw.lang.reflect.gs.ICompilableType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IRuntimeObserver
{
  /**
   * Called before the execution of each statement
   */
  public void onBeforeExecute( RuntimeInfoAtStatement ctx );

  /**
   * @return Whether the given Gosu class should be instrumented.  Note that
   * returning false here does not guarantee that the class will not be
   * instrumented, as there may be other IInstrumentors that instrument it.
   * @param gsClass
   */
  public boolean observes( ICompilableType gsClass );
}
