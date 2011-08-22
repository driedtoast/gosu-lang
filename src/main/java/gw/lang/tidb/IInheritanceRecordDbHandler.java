package gw.lang.tidb;

import java.util.Set;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface IInheritanceRecordDbHandler extends IDbHandler
{
  String IR_TABLE_NAME = ("st_" + IInheritanceRecord.class.getSimpleName()).toUpperCase();

  int getNumIndices();

  boolean add( Set<IInheritanceRecord> records);

  Set<? extends IInheritanceRecord> findDirectSupertypes(String typeName);

  Set<? extends IInheritanceRecord> findDirectSubtypes(String typeName);

  void remove(Set<IInheritanceRecord> records);

  void removeRecordsForTypes(Set<ITypeInfoFingerprintRecord> records);
}
