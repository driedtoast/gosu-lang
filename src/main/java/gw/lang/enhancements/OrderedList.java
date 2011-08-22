package gw.lang.enhancements;

import static gw.lang.enhancements.OrderedList.Direction.ASCENDING;
import static gw.lang.enhancements.OrderedList.Direction.DESCENDING;
import gw.util.IOrderedList;
import gw.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.AbstractList;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@SuppressWarnings({"UnusedDeclaration"})
public class OrderedList<T> extends AbstractList<T> implements IOrderedList<T>
{
  enum Direction{
    ASCENDING,
    DESCENDING
  }

  private List<Pair<Direction, IToComparable<T>>> _orderByBlocks = new LinkedList<Pair<Direction, IToComparable<T>>>();
  private Iterable<T> _originalValues = null;
  private List<T> _values = null;


  public OrderedList( Iterable<T> values )
  {
    _originalValues = values;
  }

  public void addOrderBy( IToComparable<T> block )
  {
    checkSort();
    checkOrderBy();
    _orderByBlocks.add( Pair.make( ASCENDING, block ) );
  }

  public void addOrderByDescending( IToComparable<T> block )
  {
    checkSort();
    checkOrderBy();
    _orderByBlocks.add( Pair.make( DESCENDING, block ) );
  }

  public OrderedList<T> addThenBy( IToComparable<T>  block )
  {
    checkSort();
    OrderedList<T> lst = new OrderedList<T>( _originalValues );
    lst._orderByBlocks = new LinkedList<Pair<Direction, IToComparable<T>>>( _orderByBlocks );
    lst._orderByBlocks.add( Pair.make( ASCENDING, block ) );
    return lst;
  }

  public OrderedList<T> addThenByDescending( IToComparable<T> block )
  {
    checkSort();
    OrderedList<T> lst = new OrderedList<T>( _originalValues );
    lst._orderByBlocks = new LinkedList<Pair<Direction, IToComparable<T>>>( _orderByBlocks );
    lst._orderByBlocks.add( Pair.make( DESCENDING, block ) );
    return lst;
  }

  public T get( int index )
  {
    maybeSort();
    return _values.get( index );
  }

  public int size()
  {
    maybeSort();
    return _values.size();
  }

  public Iterator<T> iterator()
  {
    maybeSort();
    return _values.iterator();
  }

  private void checkOrderBy()
  {
    if( _orderByBlocks.size() > 0 )
    {
      throw new IllegalStateException( "You can only orderBy() once.  After that, you must use thenBy()" );
    }
  }

  private void checkSort()
  {
    if( _values != null )
    {
      throw new IllegalStateException( "This list has already been sorted!" );
    }
  }

  private void maybeSort()
  {
    if( _values == null )
    {
      _values = new ArrayList<T>();
      for (T originalValue : _originalValues) {
        _values.add(originalValue);
      }
      Collections.sort( _values, new Comparator<T>()
      {
        public int compare( T o1, T o2 )
        {
          for( Pair<Direction, IToComparable<T>> orderByBlock : _orderByBlocks )
          {
            Comparable c1 = orderByBlock.getSecond().toComparble( o1 );
            Comparable c2 = orderByBlock.getSecond().toComparble( o2 );
            @SuppressWarnings({"unchecked"})
            int cmp = orderByBlock.getFirst() == Direction.ASCENDING ? c1.compareTo( c2 ) : c2.compareTo( c1 );
            if( cmp != 0 )
            {
              return cmp;
            }
          }
          return 0;
        }
      } );
    }
  }

  public static interface IToComparable<T>
  {
    Comparable toComparble( T elt );
  }
}
