package gw.lang.tidb;

import gw.lang.parser.CaseInsensitiveCharSequence;

import java.util.Comparator;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface ITypeInfoFingerprintRecord extends IRecord<String>
{
  Comparator<? super ITypeInfoFingerprintRecord> NAME_COMPARATOR =
    new Comparator<ITypeInfoFingerprintRecord>()
    {
      public int compare( ITypeInfoFingerprintRecord o1, ITypeInfoFingerprintRecord o2 )
      {
        return o1 == null ? -1 : o2 == null ? 1 : o1.getTypeInfoIDFeatureName().toString().compareTo( o2.getTypeInfoIDFeatureName().toString() );
      }
    };

  IFeatureInfoId getTypeInfoID();

  CaseInsensitiveCharSequence getTypeInfoIDFeatureName();

  String getFingerprint();

  boolean isNeedsToBeDiffed();

  boolean isErrant();

  void markErrant();
}
