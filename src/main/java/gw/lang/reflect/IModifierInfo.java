package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IModifierInfo
{
  int getModifiers();

  /**
   * This method exists for historical reasons only, and will be removed in future
   * releases.
   */
  @Deprecated
  void syncAnnotations( IModifierInfo modifierInfo );
}
