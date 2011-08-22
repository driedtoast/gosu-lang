package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class BooleanHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final BooleanHighPriorityCoercer INSTANCE = new BooleanHighPriorityCoercer();

  public BooleanHighPriorityCoercer()
  {
    super( BooleanCoercer.instance(), MAX_PRIORITY );
  }

  public static BooleanHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}