package gw.lang.reflect;

import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.module.IModule;

import java.net.URL;
import java.util.List;
import java.util.Set;
import java.io.File;

/**
 * A type loader is responsible for loading types. Given a fully qualified name of a type,
 * a type loader resolves the name against a set of type definitions discoverable to the 
 * loader. Type definitions may exist in any form e.g., on disk, on the network, in memory, 
 * etc. Typically, a type is resolved via its containing module's set of paths.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITypeLoader
{
  /**
   * @return The module to which this type loader belongs.
   */
  IModule getModule();

  /**
   * Gets the intrinsic type for a given class.
   *
   * @param javaClass the Class to convert to an intrinsic type
   *
   * @return the IType that corresponds to that class
   */
  IType getIntrinsicType( Class javaClass );
  
  /**
   * Gets the intrinsic type for a given class info object.
   * @param javaClassInfo the Class info object ot convert to an intrinsic type
   * @return the IType that corresponds to that class
   */
  IType getIntrinsicType(IJavaClassInfo javaClassInfo);

  /**
   * Gets a type based on a fully-qualified name.  This could either be the name of an entity,
   * like "entity.User", the name of a typekey, like "typekey.SystemPermission", or a class name, like
   * "java.lang.String".  Names can have [] appended to them to create arrays, and multi-dimensional arrays
   * are supported.<p>
   * <p/>
   * If the type can be successfully resolved by the typeloader, it will be returned, otherwise it will
   * return null.  The sole exception to this rule is the top-level TypeLoaderAccess, which will throw
   * a {@link ClassNotFoundException} if none of its composite typeloaders can load the type.<p>
   * <p/>
   * <p/>
   * There is a global lock in TypeLoaderAccess that is acquired when this method is called.  Basically
   * one type at a time can be loaded from the system.  This method is free to release that lock during this call.
   * This is needed to deal with reentrant type loaders.  It is the responsibility of this method to make sure the
   * lock is reacquired before this method returns.
   * <p/>
   * Type loader access will guarentee that no duplicate types are put into the type loader.
   *
   * @param fullyQualifiedName the fully qualified name of the type
   *
   * @return the corresponding IType or null
   */
  IType getType( String fullyQualifiedName );

  /**
   * @return the set of fully qualified type names this loader is responsible for
   *         loading. Note due to the dynamic nature of some type loaders, there is no
   *         guarantee that all types for a given loader are known at the time this
   *         method is called.
   */
  Set<? extends CharSequence> getAllTypeNames();

  /**
   * @return the set of package (aka namespace) names in which this loader's
   *         types reside.
   */
  Set<? extends CharSequence> getAllNamespaces();

  /**
   * Finds the resource with the given name.  A resource is some data
   * that can be accessed by class code in a way that may be independent
   * of the location of the code.  The exact location of the resource is
   * dependent upon the loader implementation
   * <p/>
   * <p> The name of a resource is a '<tt>/</tt>'-separated path name that
   * identifies the resource.
   *
   * @param name The resource name
   *
   * @return A <tt>URL</tt> object for reading the resource, or
   *         <tt>null</tt> if the resource could not be found or the invoker
   *         doesn't have adequate  privileges to get the resource.
   */
  URL getResource( String name );

  File getResourceFile( String name );

  /**
   * Refreshes this loader's pool of types. Types loaded subsequent to this call
   * are guarenteed to include any changes made to types prior to this call.  This
   * call should be relatively inexpensive, as it is called frequently in development
   * mode.
   */
  void refresh( boolean clearCachedTypes );

  boolean isCaseSensitive();

  List<String> getHandledPrefixes();

  boolean isNamespaceOfTypeHandled( String fullyQualifiedTypeName );

  List<Throwable> getInitializationErrors();

}
