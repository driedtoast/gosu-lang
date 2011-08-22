package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class FloatHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final FloatHighPriorityCoercer INSTANCE = new FloatHighPriorityCoercer();

  public FloatHighPriorityCoercer()
  {
    super( FloatCoercer.instance(), MAX_PRIORITY );
  }

  public static FloatHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}