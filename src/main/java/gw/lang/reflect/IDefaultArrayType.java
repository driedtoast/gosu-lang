package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IDefaultArrayType extends IType, IEnhanceableType, IHasJavaClass
{
  // returns the concrete (java) component type of this array type or
  // null if no such component type exists
  public IType getConcreteComponentType();

  // returns the concrete (java) type of this array type or null if no
  // such type exists
  public Class getConcreteClass();
}
