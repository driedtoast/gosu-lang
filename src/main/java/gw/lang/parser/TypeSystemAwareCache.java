package gw.lang.parser;

import gw.util.concurrent.Cache;
import gw.lang.reflect.AbstractTypeSystemListener;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

/**
 * This extension of the standard gw.util.concurrent.Cache class adds a type system listener
 * that will clear the cache when a type system refresh occurs.  This is useful when caches
 * have type information in them.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class TypeSystemAwareCache<K, V> extends Cache<K, V>
{

  @SuppressWarnings({"FieldCanBeLocal"})
  private final AbstractTypeSystemListener _cacheClearer = new CacheClearer(this);

  public TypeSystemAwareCache( String name, int size, MissHandler<K, V> kvMissHandler )
  {
    super( name, size, kvMissHandler );
    TypeSystem.addTypeLoaderListenerAsWeakRef( _cacheClearer );    
  }

  public static <K, V> TypeSystemAwareCache<K, V> make(String name, int size, MissHandler<K, V> handler) {
    return new TypeSystemAwareCache<K, V>(name, size, handler);
  }

  private static class CacheClearer extends AbstractTypeSystemListener
  {
    TypeSystemAwareCache _cache;

    private CacheClearer( TypeSystemAwareCache cache )
    {
      _cache = cache;
    }

    @Override
    public void refreshed()
    {
      _cache.clear();
    }

    @Override
    public Runnable createdType( String fullyQualifiedTypeName )
    {
      _cache.clear();
      return null;
    }

    @Override
    public void deletedType( String fullyQualifiedTypeName )
    {
      _cache.clear();
    }

    @Override
    public void refreshedType(IType type, boolean changed)
    {
      _cache.clear();
    }
  }
}
