package gw.lang.tidb;

import gw.util.fingerprint.FP64;
import gw.lang.reflect.ITypeLoader;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface ITypeInfoDatabaseIndexableTypeLoader extends ITypeLoader
{
  /**
   * @return Fingerprint of the given resource
   */
  FP64 getFingerprint(String fullyQualifiedTypeName);

  /**
   * @return true if the given resource should be indexed in the type info database
   */
  public boolean shouldIndex( String fullyQualifiedTypeName );

}
