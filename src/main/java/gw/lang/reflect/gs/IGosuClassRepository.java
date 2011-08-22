package gw.lang.reflect.gs;

import java.util.Set;
import java.io.Reader;
import java.net.URL;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuClassRepository
{
  /**
   * Finds the ISourceFileHandle for a given fully-qualified class name, or null if no such source file exists
   *
   * @param strQualifiedClassName the fully-qualified name of the class
   * @return The source file handle for the given class, or null if no such
   *   source file exists.
   */
  public ISourceFileHandle findClass( String strQualifiedClassName );

  /**
   * @return A set containing all type names in this repository (includes enhancement names)
   */
  public Set<String> getAllTypeNames();

  /**
   * Returns the names of all types in this repository that end with one of the specified file extensions
   *
   * @return A set containing all the type names in this repository.
   * @param extensions the set of file name extensions to consider
   */
  public Set<String> getAllTypeNames(String[] extensions);

  /**
   * Called when a new type is added to the type system
   *
   * @param fullyQualifiedTypeName the fully-qualified name of the type
   */
  void notifyOfTypeCreation( String fullyQualifiedTypeName );

  /**
   * Called when a type is refreshed
   *
   * @param fullyQualifiedTypeName the fully-qualified name of hte type
   */
  void notifyOfTypeRefresh( String fullyQualifiedTypeName );

  /**
   * Finds the given resource in this repository.
   *
   * <p> The name of a resource is a '<tt>/</tt>'-separated path name that
   * identifies the resource.
   *
   * @param name the name of the resource
   * @return the URL of the resource or null if the resource cannot be found.
   */
  URL findResource(String name);

  public Set<String> getAllNames( String strExt );

  public Reader getTemporaryTemplateResourceReader(String fullyQualifiedName);

}
