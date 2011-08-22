package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class DoubleHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final DoubleHighPriorityCoercer INSTANCE = new DoubleHighPriorityCoercer();

  public DoubleHighPriorityCoercer()
  {
    super( DoubleCoercer.instance(), MAX_PRIORITY );
  }

  public static DoubleHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}