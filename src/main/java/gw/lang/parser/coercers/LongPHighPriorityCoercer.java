package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class LongPHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final LongPHighPriorityCoercer INSTANCE = new LongPHighPriorityCoercer();

  public LongPHighPriorityCoercer()
  {
    super( LongPCoercer.instance(), MAX_PRIORITY );
  }

  public static LongPHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}