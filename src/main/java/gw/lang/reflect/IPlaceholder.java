package gw.lang.reflect;

/**
 * A general marker for any type that is a placeholder or interim type for a
 * "real" type. This is especially useful when we define and determine a type
 * dynamically from instance data.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IPlaceholder
{
  boolean isPlaceholder();
}
