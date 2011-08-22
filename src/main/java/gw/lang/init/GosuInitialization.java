package gw.lang.init;

import gw.lang.UnstableAPI;
import gw.lang.reflect.module.IModule;
import gw.util.GosuExceptionUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * The GosuInitialization class provides methods that initialize the Gosu runtime and execution environment,
 * both for use in a runtime environment and within an editing environment.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class GosuInitialization {

  private static boolean _initialized = false;

  public static boolean isInitialized() {
    return _initialized;
  }

  public static void uninitialize() {
    if (!_initialized) {
      return;
    }
    _initialized = false;
    try {
      Class cls = Class.forName("gw.internal.gosu.init.InternalGosuInit");
      Method m = cls.getMethod("uninitializeRuntime");
      m.invoke(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void initializeRuntime( List<? extends GosuPathEntry> pathEntries ) {
    if (_initialized) {
      throw new IllegalStateException("Illegal attempt to re-initialize Gosu");
    }

    try {
      Class cls = Class.forName("gw.internal.gosu.init.InternalGosuInit");
      Method m = cls.getMethod("initializeRuntime", List.class);
      m.invoke(null, pathEntries);
    } catch (Exception e) {
      throw GosuExceptionUtil.forceThrow( e );
    }
    _initialized = true;
  }

  public static void initializeMultipleModules( List<? extends IModule> modules) {
    if (_initialized) {
      throw new IllegalStateException("Illegal attempt to re-initialize Gosu");
    }

    try {
      Class cls = Class.forName("gw.internal.gosu.init.InternalGosuInit");
      Method m = cls.getMethod("initializeMultipleModules", List.class);
      m.invoke(null, modules);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    _initialized = true;
  }

}
