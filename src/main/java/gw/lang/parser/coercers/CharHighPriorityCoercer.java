package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class CharHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final CharHighPriorityCoercer INSTANCE = new CharHighPriorityCoercer();

  public CharHighPriorityCoercer()
  {
    super( CharCoercer.instance(), MAX_PRIORITY );
  }

  public static CharHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}