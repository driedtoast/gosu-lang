package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class BooleanPHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final BooleanPHighPriorityCoercer INSTANCE = new BooleanPHighPriorityCoercer();

  public BooleanPHighPriorityCoercer()
  {
    super( BooleanPCoercer.instance(), MAX_PRIORITY );
  }

  public static BooleanPHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}