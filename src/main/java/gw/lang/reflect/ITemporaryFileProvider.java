package gw.lang.reflect;

import gw.lang.reflect.gs.ISourceFileHandle;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITemporaryFileProvider {

  ISourceFileHandle getSourceFileHandle(String fullyQualifiedName, String[] extensions );
}
