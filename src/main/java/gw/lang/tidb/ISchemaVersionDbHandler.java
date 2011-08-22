package gw.lang.tidb;

import java.util.Set;
import java.sql.SQLException;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface ISchemaVersionDbHandler extends IDbHandler
{
  String SVR_TABLE_NAME = ("st_" + ISchemaVersionRecord.class.getSimpleName()).toUpperCase();
  String ID_COLUMN = "ID".toUpperCase();

  int getNumIndices();

  boolean add( Set<ISchemaVersionRecord> records);

  Set<? extends ISchemaVersionRecord> findAllSchemaVersions();

  void insertSchemaVersion() throws SQLException;

  boolean schemaVersionsDoNotMatch(int currentSchemaVersion) throws SQLException;

  void remove(Set<ISchemaVersionRecord> records);
}
