package gw.lang.tidb;

import java.util.Set;


/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface ITypeInfoFingerprintRecordDbHandler extends IDbHandler
{
  String TIFR_TABLE_NAME = ("st_" + ITypeInfoFingerprintRecord.class.getSimpleName()).toUpperCase();

  int getNumIndices();

  boolean add( Set<ITypeInfoFingerprintRecord> records);

  Set<? extends ITypeInfoFingerprintRecord> findAllRecords();

  ITypeInfoFingerprintRecord findFingerprint( IFeatureInfoId typeInfoID);

  Set<? extends ITypeInfoFingerprintRecord> findTypesThatNeedToBeDiffed();

  int updateNeedsToBeDiffed(Set<? extends IFeatureInfoId> records, boolean newValue);

  void remove(Set<ITypeInfoFingerprintRecord> records);
}
