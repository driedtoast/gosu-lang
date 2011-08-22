package gw.lang.tidb;

/**
 * Marker to represent a record (row) in the database
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface IRecord<T> 
{
  T getID();
}
