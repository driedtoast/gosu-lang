package gw.util;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class MutableBoolean
{
  private volatile boolean _bValue;

  public boolean isTrue()
  {
    return _bValue;
  }

  public void setValue( boolean bValue )
  {
    _bValue = bValue;
  }
}
