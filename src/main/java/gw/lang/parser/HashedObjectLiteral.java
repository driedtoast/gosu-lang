/* 
 * HashedObjectLiteral.java [Oct 2, 2002 10:02:27 AM]
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package gw.lang.parser;

import gw.lang.reflect.IType;

/**
 * HashedObjectLiteral facilitates keyed or otherwise hashed object referencing
 * without necessarily constructing objects of the referenced type i.e., an
 * object literal is only container for a real object's id (for comparisons and
 * assignments). It is not intended to be used as a proxy or indirect object
 * reference.
 * 
 *  Copyright 2010 Guidewire Software, Inc.
 */
public final class HashedObjectLiteral
{
  private final IType _class;
  private final long _id;

  /**
   * @param id A unique id for the object.
   */
  public HashedObjectLiteral( IType cls, long id )
  {
    _class = cls;
    _id = id;
  }

  /**
   * @return The class assignable to this literal.
   */
  public IType getAssignableClass()
  {
    return _class;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HashedObjectLiteral that = (HashedObjectLiteral) o;

    if (_id != that._id) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return (int) (_id ^ (_id >>> 32));
  }

  public long getId() {
    return _id;
  }
}
