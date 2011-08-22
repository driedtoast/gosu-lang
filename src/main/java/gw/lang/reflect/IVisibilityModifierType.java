package gw.lang.reflect;

import java.io.Serializable;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IVisibilityModifierType extends Serializable
{
  public String getName();

  public String getDisplayName();

  public boolean isConstraint();
}
