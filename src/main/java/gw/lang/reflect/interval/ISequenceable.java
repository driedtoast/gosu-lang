package gw.lang.reflect.interval;

/**
 * A sequenceable object is aware of its place in a logical sequence of similar
 * objects. Given a unit of measure and a fixed distance or step, this object
 * can produce the next and previous objects in the sequence.
 * <p\>
 * For example, a sequence of dates three months apart is a logical sequence
 * where Date is the type, Month is the unit, and 3 is the step.
 * <p\>
 * Note a sequenceable object may be defined with no concept of a unit of
 * measure e.g., number types. In this case the unit type must be defined as
 * <code>Void</code>.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ISequenceable<E extends ISequenceable<E, S, U>, S, U>
{
  E nextInSequence( S step, U unit );
  E nextNthInSequence( S step, U unit, int iIndex );
  E previousInSequence( S step, U unit );
  E previousNthInSequence( S step, U unit, int iIndex );
}
