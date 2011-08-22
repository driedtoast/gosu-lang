package gw.lang.enhancements;

import gw.lang.function.IBlock;
import java.util.Comparator;

/**
 * This class wraps around a block that returns a Comparable object to allow quick
 * block-based sorting in Gosu.  This comparator is null tolerant: nulls are considered
 * the minimal possible value and will be placed accordingly.
 * 
 * NOTE: this class is NOT thread safe.  It should not be used for parallel operations.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@SuppressWarnings({"UnusedDeclaration"}) // Used in Gosu core enhancements
public class BlockSortByComparator implements Comparator
{
  private final Object[] args = new Object[1];
  private boolean _ascending;
  private IBlock _bytecodeBlock;

  public BlockSortByComparator( Object block, boolean ascending )
  {
    _bytecodeBlock = (IBlock)block;
    _ascending = ascending;
  }

  public int compare( Object o1, Object o2 )
  {
    args[0] = o1;
    Comparable c1 = invoke( this.args );
    this.args[0] = o2;
    Comparable c2 = invoke( this.args );
    if( c1 == null && c2 == null )
    {
      return 0;
    }
    else if( c1 == null )
    {
      return _ascending ? -1 : 1;
    }
    else if( c2 == null )
    {
      return _ascending ? 1 : -1;
    }
    //noinspection unchecked
    return (_ascending ? 1 : -1 ) * c1.compareTo( c2 );
  }

  private Comparable invoke( Object[] args )
  {
    return (Comparable)_bytecodeBlock.invokeWithArgs( args );
  }
}