package gw.lang.debugger;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class RunnableWithResult
{
  private Object _result;
  private boolean _bFinished;
  private Throwable _error;

  /**
   * Just return the result of the task. Don't bother about catching exceptions
   * and all; this abstract class takes care of the dirty work.    
   */
  abstract public Object executeTask();


  //----------------------------------------------------------------------------
  // IRunnableWithResult impl

  public Object run()
  {
    try
    {
      _result = executeTask();
      return _result;
    }
    catch( Throwable t )
    {
      _error = t;
    }
    finally
    {
      _bFinished = true;
    }
    return null;
  }

  public Object getResult()
  {
    return _result;
  }

  public boolean isFinishedRunning()
  {
    return _bFinished;
  }

  public Throwable getError()
  {
    return _error;
  }
}
