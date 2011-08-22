package gw.lang.tidb;

import gw.util.IProgressCallback;
import gw.util.Predicate;

import java.sql.SQLException;
import java.util.Set;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface ITypeInfoDatabase
{
  public static enum ProcessingMode
  {
    CACHE_AGGRESSIVE,
    REGULAR,
  }

  public static final Predicate PERSISTED_FEATURE_INFO_RECORD_PREDICATE = PersistedFeatureInfoRecordPredicate.INSTANCE;


  void setLargeCacheSize() throws SQLException;

  void clearLargeCacheSize() throws SQLException;

  IFeatureInfoRecordDbHandler getFeatureInfoRecordDBHandler();

  ITypeInfoFingerprintRecordDbHandler getFingerprintDBHandler();

  IInheritanceRecordDbHandler getInheritanceDBHandler();

  ISchemaVersionDbHandler getSchemaVersionDBHandler();

  IFeatureInfoDbHandler getFeatureInfoIDDBHandler();

  /**
   * Starts the database
   */
  public void startDatabase() throws SQLException;

  public void startDatabase( boolean doGarbageCollection ) throws SQLException;

  /**
   * Stops the database
   */
  public void stopDatabase() throws SQLException;

  /**
   * Upgrades the database if it needs to be upgraded
   */
  public void upgradeIfNeeded() throws SQLException;

  /**
   * Drops the database. Used by tests.
   */
  public void dropDatabase();

  /**
   * Update optimizer statistics. "analyze" in H2.
   */
  public void updateOptimizerStatistics() throws SQLException;

  /**
   * Set the processing mode
   */
  public void setProcessingMode( ProcessingMode processingMode );

  /**
   * @return The set of TypeInfoFingerprintRecords for all fingerprints in the database. Use sparingly.
   */
  public Set<? extends ITypeInfoFingerprintRecord> getAllTypeInfoFingerprintRecords() throws SQLException;

  /**
   * Adds the given records into the database
   */
  public <T extends IRecord> void add( Set<T> records ) throws SQLException;

  /**
   * Removes the given records from the database
   */
  public <T extends IRecord> void remove( Set<T> records ) throws SQLException;

  /**
   * Removes the given records from the database
   */
  public void removeTypes( Set<ITypeInfoFingerprintRecord> records ) throws SQLException;

  /**
   * Optimization: updates the given record's offset, length, line number and column number
   */
  public void updatePosition( Set<IFeatureInfoRecordPositionUpdate> records ) throws SQLException;

  /**
   * Updates the boolean keeping track of whether a type needs to be diffed as a result of an edit
   */
  public int updateNeedsToBeDiffed( Set<IFeatureInfoId> records, boolean newValue ) throws SQLException;

  /**
   * @return A representation of a database transaction
   */
  public IDatabaseTransaction startDatabaseTransaction();

  public void createIndices( IProgressCallback progress ) throws SQLException;

}
