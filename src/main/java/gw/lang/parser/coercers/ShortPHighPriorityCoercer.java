package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ShortPHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final ShortPHighPriorityCoercer INSTANCE = new ShortPHighPriorityCoercer();

  public ShortPHighPriorityCoercer()
  {
    super( ShortPCoercer.instance(), MAX_PRIORITY );
  }

  public static ShortPHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}