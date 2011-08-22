package gw.lang.parser.coercers;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ByteHighPriorityCoercer extends PriorityDelegatingCoercer
{
  private static final ByteHighPriorityCoercer INSTANCE = new ByteHighPriorityCoercer();

  public ByteHighPriorityCoercer()
  {
    super( ByteCoercer.instance(), MAX_PRIORITY );
  }

  public static ByteHighPriorityCoercer instance()
  {
    return INSTANCE;
  }
}