package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class CharPHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final CharPHighPriorityCoercer INSTANCE = new CharPHighPriorityCoercer();

  public CharPHighPriorityCoercer()
  {
    super( CharPCoercer.instance(), MAX_PRIORITY );
  }

  public static CharPHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}