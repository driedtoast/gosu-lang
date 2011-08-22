package gw.lang.reflect.module;

import gw.lang.GosuShop;
import gw.lang.UnstableAPI;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClassLoader;
import gw.lang.shell.IUpdateableClassLoader;

import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * NOTE: This API is in flux.  This class may be moved to a different package, renamed, or have its methods change
 * in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class ModuleClassLoader extends URLClassLoader implements IUpdateableClassLoader
{
  private IModule _module;
  private IGosuClassLoader _gosuClassLoader;

  public ModuleClassLoader( URL[] urls, ClassLoader parent, IModule module )
  {
    super( urls, parent );
    init( module );
  }

  public ModuleClassLoader( URL[] urls, IModule module )
  {
    //chicken and egg: the jre's classloader should be the one from ProjectClassLoaderSupport.getClassLoader(), but it can't because this ctor is called during it's construction. To work around: make a delegating class loader that lazily gets said loader.
    //super( urls, TypeSystem.getExecutionEnvironment().getJreModule().getClassLoader() );
    super( urls );
    init( module );
  }

  public ModuleClassLoader( URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory, IModule module )
  {
    super( urls, parent, factory );
    init( module );
  }

  private void init( IModule module )
  {
    _module = module;
    _gosuClassLoader = GosuShop.createGosuClassLoader( this );
  }

  public void dumpGosuClassLoader()
  {
    _gosuClassLoader.dumpAllClasses();
    _gosuClassLoader = GosuShop.createGosuClassLoader( this );
  }

  @Override
  public void addURL( URL url )
  {
    super.addURL( url );
  }

  public IModule getModule()
  {
    return _module;
  }

  /**
   * For typeref proxy generation
   */
  public Class defineClass( String className, byte[] bytes )
  {
    TypeSystem.lock();
    try
    {
      return super.defineClass( className, bytes, 0, bytes.length );
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  @Override
  public Class<?> loadClass( String strFqn ) throws ClassNotFoundException
  {
    return loadClass( strFqn, false );
  }

  @Override
  protected synchronized Class<?> loadClass( String strFqn, boolean bResolve ) throws ClassNotFoundException
  {
    while( !TypeSystem.getGlobalLock().tryLock() )
    {
      try
      {
        //## hack:
        // This prevents deadlock ie., the call to findClass() could come back around and
        // try to grab the type sys lock. Not much we can do about this since this method
        // is called by private method ClassLoader.loadClassInternal(), which is synchronized!

        // Release this class loader's monitor, let some other thread have it (if that's the case),
        // and try again to acquire the type sys lock. The idea is to prevent deadlock by ensuring
        // we can acquire both locks or none at all... albeit expensively.
        wait( 100 );
      }
      catch( InterruptedException e )
      {
        throw new RuntimeException( e );
      }
    }

    try
    {
      return _module.loadClass( strFqn, bResolve );
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  /**
   * Load the class with the fully qualified name from this module's classpath.
   * Dependencies are not searched.
   *
   * @param strFqn The fully qualified name of the class to load
   *
   * @return The class
   */
  public Class<?> loadClassLocally( String strFqn, boolean bResolve ) throws ClassNotFoundException
  {
    int iDims = getArrayDims( strFqn );
    if( iDims > 0 )
    {
      if( strFqn.length() != iDims + 1 )
      {
        strFqn = strFqn.substring( iDims + 1, strFqn.length() - 1 );
      }
      else // primitive array, can load directly from sys loader
      {
        return Class.forName( strFqn, bResolve, ClassLoader.getSystemClassLoader() );
      }
    }
    Class cls;
    try
    {
      // First try to load the class from our default class loader. 99.99% of
      // time the class will come from there. Only the classes we define via
      // Javassist live outside the default loader.

      cls = super.loadClass( strFqn, bResolve );
    }
    catch( ClassNotFoundException cnfe )
    {
      cls = loadGosuRelatedClass( strFqn, cnfe );
      if( cls == null )
      {
        throw cnfe;
      }
    }
    if( cls != null && iDims > 0 )
    {
      cls = Array.newInstance( cls, new int[iDims] ).getClass(); // barf
    }
    return cls;
  }

  private int getArrayDims( String strFqn )
  {
    int iDims = 0;
    for( int i = 0; i < strFqn.length(); i++ )
    {
      char c = strFqn.charAt( i );
      if( c == '[' )
      {
        iDims++;
      }
    }
    return iDims;
  }

  public IGosuClassLoader getGosuClassLoader()
  {
    return _gosuClassLoader;
  }

  private Class<?> loadGosuRelatedClass( String strFqn, ClassNotFoundException cnfe ) throws ClassNotFoundException
  {
    Class<? extends ITypeRef> typeClass = _module.getModuleTypeLoader().getTypeRefFactory().getOrCreateTypeProxy( strFqn );

    if( typeClass != null )
    {
      return typeClass;
    }

    throw cnfe;
  }

  public URL getResourceLocally( String name )
  {
    return super.getResource( name );
  }

  @Override
  public URL getResource( String name )
  {
    return _module.getResource( name );
  }
}
