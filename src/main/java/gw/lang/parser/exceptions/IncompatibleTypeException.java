package gw.lang.parser.exceptions;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class IncompatibleTypeException extends RuntimeException
{
  public IncompatibleTypeException()
  {
  }

  public IncompatibleTypeException( String message )
  {
    super( message );
  }

  public IncompatibleTypeException( Throwable cause )
  {
    super( cause );
  }

  public IncompatibleTypeException( String message, Throwable cause )
  {
    super( message, cause );
  }
}
