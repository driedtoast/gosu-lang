package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class BytePHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final BytePHighPriorityCoercer INSTANCE = new BytePHighPriorityCoercer();

  public BytePHighPriorityCoercer()
  {
    super( BytePCoercer.instance(), MAX_PRIORITY );
  }

  public static BytePHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}