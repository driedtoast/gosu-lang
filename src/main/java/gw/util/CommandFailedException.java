package gw.util;

/**
 * An exception that indicates that a shell exited with a non-zero return value.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class CommandFailedException extends RuntimeException
{
  private int _errorCode;

  public CommandFailedException( int i, String msg )
  {
    super( msg );
    _errorCode = i;
  }

  public int getErrorCode()
  {
    return _errorCode;
  }
  
}
