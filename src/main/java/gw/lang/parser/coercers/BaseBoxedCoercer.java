package gw.lang.parser.coercers;

import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class BaseBoxedCoercer extends StandardCoercer
{
  @Override
  public int getPriority( IType to, IType from )
  {
    return 2;
  }
}
