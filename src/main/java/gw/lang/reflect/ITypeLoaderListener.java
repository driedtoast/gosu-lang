package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITypeLoaderListener
{
  /**
   * Fired when a new type is created
   */
  public Runnable createdType( String fullyQualifiedTypeName );

  /**
   * Fired when an existing type is deleted
   */
  public void deletedType( String fullyQualifiedTypeName );

  /**
   * Fired when an existing type is refreshed, i.e. there are potential changes
   */
  public void refreshedType(IType type, boolean changed);

  /**
   * Fired when type loaders are refreshed
   */
  public void refreshed();
}
