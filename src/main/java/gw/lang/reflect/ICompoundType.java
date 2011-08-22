package gw.lang.reflect;

import java.util.Set;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ICompoundType extends IType
{
  Set<IType> getTypes();
}
