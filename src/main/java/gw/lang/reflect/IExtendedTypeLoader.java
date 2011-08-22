package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IExtendedTypeLoader extends ITypeLoader
{
  /**
   * Returns the intrinsic type for the given Object.
   *
   * @param object the object to get an IType for
   *
   * @return the IType for the object
   */
  public IType getIntrinsicTypeFromObject( Object object );
}