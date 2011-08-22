package gw.lang.parser.coercers;

import gw.lang.parser.ICoercer;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class PriorityDelegatingCoercer extends BaseCoercer
{
  private final ICoercer _delegate;
  private final int _priority;

  public PriorityDelegatingCoercer( ICoercer delegate, int priority )
  {
    _delegate = delegate;
    _priority = priority;
  }

  public final Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return _delegate.coerceValue( typeToCoerceTo, value );
  }

  public boolean isExplicitCoercion()
  {
    return _delegate.isExplicitCoercion();
  }

  public boolean handlesNull()
  {
    return _delegate.handlesNull();
  }

  public int getPriority( IType to, IType from )
  {
    return _priority;
  }
}
