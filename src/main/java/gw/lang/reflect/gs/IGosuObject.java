package gw.lang.reflect.gs;

import gw.lang.reflect.IType;

/**
 * @dontgeneratetypeinfo
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuObject
{
  IType getIntrinsicType();

  //
  // Methods cooresponding with java.lang.Object
  //

  public String toString();

  public int hashCode();

  public boolean equals( Object o );

}
