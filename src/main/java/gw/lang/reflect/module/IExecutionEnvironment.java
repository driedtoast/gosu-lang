package gw.lang.reflect.module;

import gw.fs.IFile;
import gw.lang.UnstableAPI;

import java.util.List;
import java.net.URL;

/**
 * NOTE: This API is in flux.  This class may be moved to a different package, renamed, or have its methods change
 * in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IExecutionEnvironment
{
  List<? extends IModule> getModules();
  void setModules( List<? extends IModule> modules );
  void addModule(IModule module);
  void removeModule(IModule module);

  IModule getDefaultModule();
  void setDefaultModule(IModule defaultModule);

  void pushModule( IModule module );
  void popModule( IModule module );
  IModule getCurrentModule();
  IModule getModule( String strModuleName );
  IModule getModule( IFile file );
  IModule getModule( URL baseURL );

  IModule getJreModule();

  List<String> getTypeLoadersFromProgram();
  void setTypeLoadersFromProgram( List<String> typeLoadersFromProgram );

  /**
   * @return Whether or not this is the default single module environment.
   */
  boolean isDefault();


  void setLog(Object log);
  void logI(String fqn, String message);
  void logI(String fqn, String message, Throwable t);

}
