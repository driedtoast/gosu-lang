package gw.lang.tidb;

import gw.lang.reflect.ITypeInfo;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface SourceDiffHandler
{
  /**
   * Determines if there are any difference between the current version of the given type info and the known
   * FeatureInfoRecords in the database. Then processes these changes.
   *
   * @param transaction if not null, the given transaction is used
   */
  public void handleSourceDiff( IFeatureInfoId typeInfoID, IDatabaseTransaction transaction );

  public void handleSourceDiff( String fullyQualifiedTypeName, IDatabaseTransaction transaction );

  public void handleSourceDiff( IFeatureInfoId typeInfoID );

  public void handleSourceDiff( String fullyQualifiedTypeName );

  public void handleSourceDiff( ITypeInfo typeInfo );

  public void notifyStartupComplete();

  /**
   * Diffs all the errant types
   */
  public void diffTypesThatNeedProcessing();

  /**
   * Finds all errant types and marks them as dirty for later processing
   */
  public void markErrantTypesAsNeedsProcessing();
}
