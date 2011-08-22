package gw.config;

import gw.lang.IGosuShop;
import gw.lang.parser.ICoercionManager;
import gw.lang.parser.IGosuParserFactory;
import gw.lang.reflect.IEntityAccess;
import gw.lang.reflect.ITypeSystem;
import gw.lang.reflect.module.IFileSystem;
import gw.lang.tidb.ITypeInfoDatabaseInit;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.lang.reflect.Method;

/**
 * <p>This is a canonical implementation of a ServiceKernel.  It is a singleton and overrides
 * the {@link ServiceKernel#defineServices()} and
 * {@link ServiceKernel#redefineServices()} methods to define and register custom 
 * implementations of services respectively.</p>
 *                                                   
 * <p>It also provides convenience methods for each service it provides, which makes it easier to discover exactly
 * what services this kernel provides</p>
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class CommonServices extends ServiceKernel
{
  private static CommonServices _kernel = new CommonServices();
  private static ITypeSystem _typeSystem;  //maintained outside the kernel for perf reasons
  private static IFileSystem _fileSystem = getDefaultFileSystemInstance(); // Currently not technically a service, since it needs to be available all the time

  private CommonServices()
  {
    Registry.addLocationListener( new ChangeListener()
    {
      public void stateChanged( ChangeEvent e )
      {
        _kernel.resetKernel();
      }
    } );
  }

  protected void defineServices()
  {
    _kernel = this;
    
    try
    {
      defineService( IEntityAccess.class, (IEntityAccess)Class.forName( "gw.internal.gosu.parser.DefaultEntityAccess" ).newInstance() );
      _typeSystem = (ITypeSystem)Class.forName( "gw.internal.gosu.parser.TypeSystemImpl" ).newInstance();
      defineService( ICoercionManager.class, (ICoercionManager)Class.forName( "gw.lang.parser.StandardCoercionManager" ).newInstance() );
      defineService( IGosuParserFactory.class, (IGosuParserFactory)Class.forName( "gw.internal.gosu.parser.GosuParserFactoryImpl" ).newInstance() );
      defineService( IGosuShop.class, (IGosuShop)Class.forName( "gw.internal.gosu.parser.GosuIndustrialParkImpl" ).newInstance() );
      defineService( ITypeInfoDatabaseInit.class, (ITypeInfoDatabaseInit)Class.forName( "gw.internal.gosu.tidb.TypeInfoDatabaseInitImpl" ).newInstance() );
      defineService( IGosuLocalizationService.class, (IGosuLocalizationService)Class.forName("gw.internal.gosu.DefaultLocalizationService").newInstance());
      defineService( IXmlSchemaCompatibilityConfig.class, (IXmlSchemaCompatibilityConfig)Class.forName( "gw.config.DefaultXmlSchemaCompatibilityConfig" ).newInstance() );
      defineService( IJavaClassInfoProvider.class, (IJavaClassInfoProvider)Class.forName( "gw.internal.gosu.parser.DefaultJavaClassInfoProvider" ).newInstance() );
      defineService( IGosuInitializationHooks.class, new DefaultGosuInitializationHooks() );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    catch( NoClassDefFoundError e)  {
      e.printStackTrace();
      throw e;
    }
  }

  private static IFileSystem getDefaultFileSystemInstance() {
    try {
      Class cls = Class.forName("gw.internal.gosu.module.fs.FileSystemImpl");
      Method m = cls.getMethod("getInstance");
      return (IFileSystem) m.invoke(null);
    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  protected void redefineServices()
  {
    redefineServicesWithClass( Registry.instance().getCommonServiceInit() );
  }

  public static IEntityAccess getEntityAccess()
  {
    return _kernel.getService( IEntityAccess.class );
  }

  public static ICoercionManager getCoercionManager()
  {
    return _kernel.getService( ICoercionManager.class );
  }

  public static ITypeSystem getTypeSystem()
  {
    return _typeSystem;
  }

  public static IGosuParserFactory getGosuParserFactory()
  {
    return _kernel.getService( IGosuParserFactory.class );
  }

  public static IGosuShop getGosuIndustrialPark()
  {
    return _kernel.getService( IGosuShop.class );
  }

  public static ITypeInfoDatabaseInit getTypeInfoDatabaseInit()
  {
    return _kernel.getService( ITypeInfoDatabaseInit.class );
  }

  public static IGosuLocalizationService getGosuLocalizationService()
  {
    return _kernel.getService( IGosuLocalizationService.class );
  }

  public static IXmlSchemaCompatibilityConfig getXmlSchemaCompatibilityConfig() {
    return _kernel.getService( IXmlSchemaCompatibilityConfig.class );
  }

  public static IJavaClassInfoProvider getJavaClassInfoProvider() {
    return _kernel.getService( IJavaClassInfoProvider.class );
  }


  public static IGosuInitializationHooks getGosuInitializationHooks() {
    return _kernel.getService( IGosuInitializationHooks.class );
  }

  public static IFileSystem getFileSystem() {
    return _fileSystem;
  }
}
