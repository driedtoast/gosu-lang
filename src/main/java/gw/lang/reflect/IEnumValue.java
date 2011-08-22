package gw.lang.reflect;

/**
 * Represents a single code/value pair
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IEnumValue extends IEnumConstant
{
  /**
   * @return The enum constant instance
   */
  public Object getValue();
}