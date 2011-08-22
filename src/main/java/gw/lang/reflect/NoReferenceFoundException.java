package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class NoReferenceFoundException extends RuntimeException
{
  private IType _type;

  public NoReferenceFoundException( IType type )
  {
    super( type.getName() );
    _type = type;
  }

  public IType getType()
  {
    return _type;
  }
}
