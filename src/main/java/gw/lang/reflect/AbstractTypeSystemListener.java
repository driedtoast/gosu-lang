package gw.lang.reflect;

/**
 * A helper class so not all methods of the ITypeLoaderListener interface
 * need be implemented.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class AbstractTypeSystemListener implements ITypeLoaderListener
{
  @Override
  public Runnable createdType( String fullyQualifiedTypeName )
  {
    return null;
  }

  @Override
  public void deletedType( String fullyQualifiedTypeName )
  {
  }

  @Override
  public void refreshedType(IType type, boolean changed)
  {
  }

  @Override
  public void refreshed()
  {
  }
}
