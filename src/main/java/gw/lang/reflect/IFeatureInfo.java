package gw.lang.reflect;

/**
 * This is a base interface for all type information features. Examples of type
 * information features include properties, methods, constructors, and events.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IFeatureInfo
{
  /**
   * Get this feature's containing feature. May be null.
   */
  public IFeatureInfo getContainer();

  /**
   * Get this feature's owner's type. If this feature has no owner, this is the
   * type of the feature itself. Typically, the only kind of feature w/o an
   * owner is an ITypeInfo, so properties, methods, params, etc. s/b considered
   * owned. Hence, ultimately an ITypeInfo's type will be the owner's type for
   * any given feature.
   */
  public IType getOwnersType();

  /**
   * Gets the programmatic name or identifier of this feature. Note this name
   * should follow Java identifier naming conventions (alpha-num and
   * underscores, but can't start with a digit).
   */
  public String getName();

  /**
   * Gets the display name of this feature. There is no guarantee this display
   * name is localized.
   */
  public String getDisplayName();

  /**
   * A full description of this feature.
   */
  public String getDescription();
}
