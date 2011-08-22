package gw.lang.tidb;

import java.sql.SQLException;
import java.util.Set;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface IDatabaseTransaction
{
  /**
   * Adds the given records into the database
   */
  public <T extends IRecord> void add( Set<T> records ) throws SQLException;

  /**
   * Removes the given records from the database
   */
  public <T extends IRecord> void remove( Set<T> records ) throws SQLException;

  /**
   * Optimization: updates the given record's offset, length, line number and column number
   *
   * @return The number of records updated
   */
  public void updatePosition( Set<? extends IFeatureInfoRecordPositionUpdate> records ) throws SQLException;

  /**
   * Flips the boolean keeping track of whether a type needs to be diffed as a result of an edit
   */
  public int updateNeedsToBeDiffed( Set<? extends IFeatureInfoId> records, boolean newValue ) throws SQLException;

  /**
   * Rollback transaction
   */
  public void rollback() throws SQLException;

  /**
   * Commit transaction
   */
  public void commit() throws SQLException;
}
