package gw.lang.parser.coercers;

import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class IdentityCoercer extends BaseCoercer
{
  private static final IdentityCoercer INSTANCE = new IdentityCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return value;
  }

  public boolean isExplicitCoercion()
  {
    return false;
  }

  public boolean handlesNull()
  {
    return true;
  }

  public int getPriority( IType to, IType from )
  {
    return 0;
  }

  public static IdentityCoercer instance()
  {
    return INSTANCE;
  }
}
