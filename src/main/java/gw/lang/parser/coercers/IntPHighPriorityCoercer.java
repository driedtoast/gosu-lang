package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class IntPHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final IntPHighPriorityCoercer INSTANCE = new IntPHighPriorityCoercer();

  public IntPHighPriorityCoercer()
  {
    super( IntPCoercer.instance(), MAX_PRIORITY );
  }

  public static IntPHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}