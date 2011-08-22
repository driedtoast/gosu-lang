package gw.lang.reflect.module;

import gw.fs.IDirectory;
import gw.lang.init.GosuPathEntry;
import gw.lang.reflect.ITypeLoader;
import gw.lang.UnstableAPI;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * NOTE: This API is in flux.  This class may be moved to a different package, renamed, or have its methods change
 * in future releases.
 *
 * A logical module corresponding with a native module defined within the
 * execution environment hosting Gosu. For example, a concrete class
 * implementing this interface is a direct mapping to a project or module 
 * defined in an IDE.
 * <p>
 * A module defines a set of paths having resources and libraries and also 
 * defines dependency relationships to other modules and libraries.
 * <p>
 * This interface is not intended to be implemented by clients. An instance
 * of one of these is obtained via {@link gw.lang.GosuShop#createModule( String )}
 * 
 * @see IExecutionEnvironment
 * @see Dependency
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IModule extends IResourceContainer
{
  public final static String DEFAULT_SINGLE_MODULE_NAME = "_default_";
  
  /**
   * @return A unique name relative to all other modules in a given execution 
   *   environment.
   */
  String getName();

  /**
   * Returns the module root for this module, i.e. where the module.xml file lives.
   * @return the module root for this module
   */
  IDirectory getRoot();
  void setRoot(IDirectory rootDir);

  /**
   * @return A list of dependencies for this module. The list may contain both 
   *   libraries and other modules. The dependency graph must not have cycles. 
   */
  List<Dependency> getDependencies();
  void addDependency( Dependency dependency );
  void removeDependency( Dependency d );
  void removeDependency( IModule module );
  void clearDependencies();

  ITypeLoaderStack getModuleTypeLoader();

  /**
   * @return The path[s] having source files that should be exposed to this 
   *   module.
   */
  List<IDirectory> getSourcePath();
  void setSourcePath( List<IDirectory> path );

  /**
   * @return The set of all paths having resources: source path, output path, 
   *   and library dependency paths. No module dependencies are included; the 
   *   resource path for a give module does not include resource paths of 
   *   dependent modules. 
   */
  public List<IDirectory> getResourcePath();

  /**
   * Gets an IResourceAccess object that spans all resource folders specified.  Note that this may not
   * be appropriate for all typeloaders, as not all typeloaders are necessarily specified in all module
   * definitions.
   *
   * @return
   */
  IResourceAccess getResourceAccess();

  /**
   * The class loader corresponding with this module. This loader is responsible 
   * for loading classes that are directly defined in this module's resource 
   * path. Note core JRE classes are contained in a separate module that is 
   * shared by all other modules. Thus, this module's class loader does not load 
   * any core JRE classes.  
   */
  ModuleClassLoader getClassLoader();
  void setClassLoader( ModuleClassLoader classLoader );

  Class<?> loadClass( String strJavaClassName ) throws ClassNotFoundException;
  Class<?> loadClass( String strJavaClassName, boolean bResolve ) throws ClassNotFoundException;

  /**
   * @param paths The class paths this module's Java class loader uses.
   */
  void setJavaClasspath( List<URL> paths );
  void setJavaClasspathFromFiles( List<String> paths );  
  List<String> getJavaClassPath();

  /**
   * @return The module/project from the execution environment that corresponds
   *   with this logical module. For example, in Eclipse the native module is of 
   *   type IJavaProject.
   */
  Object getNativeModule();
  void setNativeModule( Object module );

  String getClassNameForFile(File classFile);
  String getTemplateNameForFile(File templateFile);
  String getProgramNameForFile(File programFile);

  public abstract IClassPath.ClassPathInfo getClassNamesFromClassPath();

  URL getResource(String name);
  void callAll( Runnable preOrderOp, Runnable postOrderOp );

  void addTypeLoader(ITypeLoader typeLoader);
  List<ITypeLoader> getTypeLoaders();

  public void setTypeloaderClassLoader(ClassLoader typeloaderClassLoader);

  void refreshTypeloaders();
  
  void addPathEntry(GosuPathEntry pathEntry);

  /**
   * Returns typeloaders of the given class that are local to this module.
   *
   * @param typeLoaderClass
   * @param <T>
   * @return
   */
  <T extends ITypeLoader> List<? extends T>  getLocalTypeLoaders(Class<T> typeLoaderClass);

  /**
   * Returns typeloaders of the given class that are local to this module as well as such
   * typeloaders from dependent modules.
   *
   * @param typeLoaderClass
   * @param <T>
   * @return
   */
  <T extends ITypeLoader> List<? extends T>  getAllTypeLoaders(Class<T> typeLoaderClass);

}
