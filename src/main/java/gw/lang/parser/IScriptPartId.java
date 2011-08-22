package gw.lang.parser;

import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class IScriptPartId
{
  /**
   * @return The type that contains this part.
   */
  public abstract IType getContainingType();

  /**
   * @return The name of the type that contains this part.
   */
  public abstract String getContainingTypeName();

  /**
   * @return An id that distinguishes this part from other parts
   *         in the containing type.
   */
  public abstract String getId();

  public abstract String toString();
}
