package gw.lang.reflect.interval;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public final class LongInterval extends NumberInterval<Long, LongInterval>
{
  @SuppressWarnings({"UnusedDeclaration"})
  public LongInterval( Long left, Long right )
  {
    this( left, right, 1, true, true, false );
  }

  public LongInterval( Long left, Long right, long lStep, boolean bLeftClosed, boolean bRightClosed, boolean bReverse )
  {
    super( left, right, lStep, bLeftClosed, bRightClosed, bReverse );

    if( lStep <= 0 )
    {
      throw new IllegalArgumentException( "The step must be greater than 0: " + lStep );
    }
  }

  @Override
  public Iterator<Long> iterateFromLeft()
  {
    return new ForwardIterator();
  }

  @Override
  public Iterator<Long> iterateFromRight()
  {
    return new ReverseIterator();
  }

  @Override
  public Long getFromLeft( int iStepIndex )
  {
    if( iStepIndex < 0 )
    {
      throw new IllegalArgumentException( "Step index must be >= 0: " + iStepIndex );
    }

    if( !isLeftClosed() )
    {
      iStepIndex++;
    }    
    long value = getLeftEndpoint() + getStep() * iStepIndex;
    if( isRightClosed() ? value <= getRightEndpoint() : value < getRightEndpoint() )
    {
      return value;
    }

    return null;
  }

  @Override
  public Long getFromRight( int iStepIndex )
  {
    if( iStepIndex < 0 )
    {
      throw new IllegalArgumentException( "Step index must be >= 0: " + iStepIndex );
    }

    if( !isRightClosed() )
    {
      iStepIndex++;
    }    
    long value = getRightEndpoint() - getStep() * iStepIndex;
    if( isLeftClosed() ? value >= getLeftEndpoint() : value > getLeftEndpoint() )
    {
      return value;
    }

    return null;
  }

  public class ForwardIterator extends AbstractLongIterator
  {
    private long _csr;

    public ForwardIterator()
    {
      _csr = getLeftEndpoint();
      if( !isLeftClosed() && hasNext() )
      {
        next();
      }
    }

    @Override
    public boolean hasNext()
    {
      return _csr < getRightEndpoint() || (isRightClosed() && _csr == getRightEndpoint());
    }

    @Override
    public Long next()
    {
      return nextLong();
    }
    public long nextLong()
    {
      if( _csr > getRightEndpoint() ||
          (!isRightClosed() && _csr == getRightEndpoint()) )
      {
        throw new NoSuchElementException();
      }
      long ret = _csr;
      _csr = _csr + getStep();
      return ret;
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }

  private class ReverseIterator extends AbstractLongIterator
  {
    private long _csr;

    public ReverseIterator()
    {
      _csr = getRightEndpoint();
      if( !isRightClosed() && hasNext() )
      {
        next();
      }
    }

    @Override
    public boolean hasNext()
    {
       return _csr > getLeftEndpoint() || (isLeftClosed() && _csr == getLeftEndpoint());
    }

    @Override
    public Long next()
    {
      return nextLong();
    }
    public long nextLong()
    {
      if( _csr < getLeftEndpoint() ||
          (!isLeftClosed() && _csr == getLeftEndpoint()) )
      {
        throw new NoSuchElementException();
      }
      long ret = _csr;
      _csr = _csr - getStep();
      return ret;
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}