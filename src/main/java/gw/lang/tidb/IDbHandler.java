package gw.lang.tidb;

import gw.util.IProgressCallback;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface IDbHandler
{
  void createTable();

  void cleanup();

  void createIndices( IProgressCallback progress);

  boolean doesTableMatch();

  void dropTable();

  void startup(String dbName);
}
