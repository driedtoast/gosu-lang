package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class FloatPHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final FloatPHighPriorityCoercer INSTANCE = new FloatPHighPriorityCoercer();

  public FloatPHighPriorityCoercer()
  {
    super( FloatPCoercer.instance(), MAX_PRIORITY );
  }

  public static FloatPHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}