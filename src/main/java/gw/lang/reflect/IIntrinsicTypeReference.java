package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IIntrinsicTypeReference
{
  /**
   * The type of this feature e.g., for a property this is the property's type.
   */
  public IType getFeatureType();
}
