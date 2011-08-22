package gw.lang.reflect.gs;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuClassLoader
{
  Class<?> findClass( String strName ) throws ClassNotFoundException;

  Class<?> getFunctionClassForArity( int length );

  void dispose( Class<?> javaClass );

  /**
   * Reloads all classes that been disposed via the dispose(Class) method since the last time this method was called.
   * This method will be a no-op if BytecodeOptions.canReloadClasses() returns false.
   *
   * @return the results of the class redefinition attempt
   */
  ReloadResults reloadDisposedClasses();

  void dumpAllClasses();

  /**
   * Reloads all classes that have changed on disk since they were initially loaded.  This is done by tracking the
   * associated file's timestamp at the time that the class is compiled, and then comparing the current timestamps
   * on all loaded classes to see if they've changed.  This method will be a no-op if BytecodeOptions.canReloadClasses()
   * returns false.
   *
   * @return the results of the class redefinition attempt
   */
  ReloadResults reloadChangedClasses();
}
