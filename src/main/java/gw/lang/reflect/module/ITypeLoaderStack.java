package gw.lang.reflect.module;

import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.ITypeRefFactory;
import gw.lang.UnstableAPI;

import java.util.List;

/**
 * NOTE: This API is in flux.  This class may be moved to a different package, renamed, or have its methods change
 * in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface ITypeLoaderStack
{
  List<ITypeLoader> getTypeLoaderStack();

  ITypeRefFactory getTypeRefFactory();

  IModule getModule();
  
  <T extends ITypeLoader> T getTypeLoader( Class<? extends T> loaderType );

  void refresh(boolean clearCachedTypes);
}
