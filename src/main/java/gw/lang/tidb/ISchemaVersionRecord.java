package gw.lang.tidb;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface ISchemaVersionRecord extends IRecord<Integer>
{
  int CURRENT_SCHEMA_VERSION = 11;

  Integer getID();
}
