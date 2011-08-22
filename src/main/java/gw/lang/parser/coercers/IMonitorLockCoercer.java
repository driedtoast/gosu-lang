package gw.lang.parser.coercers;

import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class IMonitorLockCoercer extends BaseCoercer
{
  private static final IMonitorLockCoercer INSTANCE = new IMonitorLockCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return value;
  }

  public boolean isExplicitCoercion()
  {
    return true;
  }

  public boolean handlesNull()
  {
    return true;
  }

  public int getPriority( IType to, IType from )
  {
    return 0;
  }

  public static IMonitorLockCoercer instance()
  {
    return INSTANCE;
  }
}