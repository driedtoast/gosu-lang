package gw.lang.reflect.interval;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class NumberInterval<E extends Number & Comparable<E>, ME extends NumberInterval<E, ME>> extends IterableInterval<E, E, Void, ME>
{
  @SuppressWarnings({"UnusedDeclaration"})
  public NumberInterval( E left, E right, E step )
  {
    this( left, right, step, true, true, false );
  }

  public NumberInterval( E left, E right, E step, boolean bLeftClosed, boolean bRightClosed, boolean bReverse )
  {
    super( left, right, step, null, bLeftClosed, bRightClosed, bReverse );
  }
}