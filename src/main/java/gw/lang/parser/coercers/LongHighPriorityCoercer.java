package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class LongHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final LongHighPriorityCoercer INSTANCE = new LongHighPriorityCoercer();

  public LongHighPriorityCoercer()
  {
    super( LongCoercer.instance(), MAX_PRIORITY );
  }

  public static LongHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}