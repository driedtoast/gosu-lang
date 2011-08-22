package gw.lang.parser.coercers;

import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class StandardCoercer extends BaseCoercer
{
  public boolean isExplicitCoercion()
  {
    return true;
  }

  public boolean handlesNull()
  {
    return false;
  }

  public int getPriority( IType to, IType from )
  {
    return 0; // lowest priority
  }
}