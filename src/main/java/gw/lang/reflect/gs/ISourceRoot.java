package gw.lang.reflect.gs;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ISourceRoot
{
  /**
   * @return An id that distinguishes this source root from other source roots
   *         in the containing resource. Can be null.
   */
  public String getId();

  /**
   * @return The source code corresponding with this source root.
   */
  public String getSource();
}
