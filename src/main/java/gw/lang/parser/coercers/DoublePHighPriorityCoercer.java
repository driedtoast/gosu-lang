package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class DoublePHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final DoublePHighPriorityCoercer INSTANCE = new DoublePHighPriorityCoercer();

  public DoublePHighPriorityCoercer()
  {
    super( DoublePCoercer.instance(), MAX_PRIORITY );
  }

  public static DoublePHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}