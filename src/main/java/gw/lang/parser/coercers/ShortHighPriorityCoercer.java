package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ShortHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final ShortHighPriorityCoercer INSTANCE = new ShortHighPriorityCoercer();

  public ShortHighPriorityCoercer()
  {
    super( ShortCoercer.instance(), MAX_PRIORITY );
  }

  public static ShortHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}