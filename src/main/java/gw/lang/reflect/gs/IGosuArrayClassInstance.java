package gw.lang.reflect.gs;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuArrayClassInstance extends IGosuObject
{
  int getLength();

  IGosuObject getArrayComponent( int iIndex );

  void setArrayComponent( int iIndex, IGosuObject value );

  IGosuObject[] getObjectArray();

  Object[] toArray();
}
