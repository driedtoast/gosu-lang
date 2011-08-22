package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class IntHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final IntHighPriorityCoercer INSTANCE = new IntHighPriorityCoercer();

  public IntHighPriorityCoercer()
  {
    super( IntCoercer.instance(), MAX_PRIORITY );
  }

  public static IntHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}