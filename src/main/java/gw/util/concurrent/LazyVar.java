package gw.util.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Wraps the controversial double-checked locking pattern in a
 * typesafe holder.  Subclasses should anonymously override
 * this class and provide the initialization of the variable.</p>
 *
 * <p>If you are lucky enough to be writing code in gosu, you can
 * take advantage of the {@link LazyVar#make(gw.util.concurrent.LazyVar.LazyVarInit)} method:
 * <pre>
 *    var lazyVar = LazyVar.make( \-> new ArrayList<String>() )
 * </pre>
 * This helps avoid generic annotation madness.
 *
 * Copyright 2010 Guidewire Software, Inc.
 */
public abstract class LazyVar<T>
{
  private final static Object NULL = new Object();

  private volatile T _val = null;
  private final Lock _lock;

  /**
   * Constructs a LazyVar that will use itself as the object of synchronization.
   */
  public LazyVar()
  {
    _lock = new ReentrantLock();
  }

  /**
   * Constructs a LazyVar that will synchronize on the given object.
   */
  protected LazyVar( Lock lock )
  {
    _lock = lock;
  }

  /**
   * @return the value of this lazy var, created if necessary
   */
  public final T get()
  {
    T result = _val;
    if(result == NULL) {
      return null;
    }
    if( result == null )
    {
      _lock.lock();
      try{

        result = _val;
        if(result == NULL) {
          return null;
        }
        if( result == null )
        {
          result = init();

          //The extra space makes all the difference

          if (result == null) {
            _val = (T)NULL;
          } else {
            _val = result;
          }
        }
      } finally {
        _lock.unlock();
      }
    }
    return result;
  }

  /**
   * Clears the variable, forcing the next call to {@link #get()} to re-calculate
   * the value.
   */
  public final T clear()
  {
    T hold;
    _lock.lock();
    try
    {
      hold = _val;
      _val = null;
    } finally {
      _lock.unlock();
    }
    return hold;
  }

  public final void clearNoLock()
  {
    _val = null;
  }

  protected void initDirectly( T val )
  {
    _lock.lock();
    try
    {
      _val = val;
    }
    finally
    {
      _lock.unlock();
    }
  }

  /**
   * @return the created value
   */
  protected abstract T init();

  public boolean isLoaded() {
    return _val != null;
  }

  /**
   * A simple init interface to make LazyVar's easier to construct
   * from gosu.
   */
  public static interface LazyVarInit<Q> {
    public Q init();
  }

  /**
   * Creates a new LazyVar based on the type of the LazyVarInit passed in.
   * This method is intended to be called with blocks from Gosu.
   */
  public static <Q> LazyVar<Q> make( final LazyVarInit<Q> init ) {
    return new LazyVar<Q>(){
      protected Q init()
      {
        return init.init();
      }
    };
  }

  /**
   * Creates a new LazyVar based on the type of the LazyVarInit passed in.
   * This method is intended to be called with blocks from Gosu.
   */
  public static <Q> LazyVar<Q> makeWithLock( Lock lock, final LazyVarInit<Q> init ) {
    return new LazyVar<Q>(lock){
      protected Q init()
      {
        return init.init();
      }
    };
  }

}