package gw.lang.parser;

/**
 * Allows a level of indirect access to a symbol value.
 *
 * WARNING: this class will be removed in a future release
 * 
 *  Copyright 2010 Guidewire Software, Inc.
 */
@Deprecated
public class BoxedValue
{
  Object _value;

  public BoxedValue( Object value )
  {
    _value = value;
  }

  public Object getValue()
  {
    return _value;
  }

  public void setValue( Object value )
  {
    _value = value;
  }

  @Override
  public String toString()
  {
    return "Reference : " + _value;
  }
}