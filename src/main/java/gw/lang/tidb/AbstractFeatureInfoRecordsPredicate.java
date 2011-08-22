package gw.lang.tidb;

import gw.util.Predicate;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public abstract class AbstractFeatureInfoRecordsPredicate implements Predicate
{
  protected AbstractFeatureInfoRecordsPredicate() {
  }

  public final boolean evaluate(Object object) {
    if (!(object instanceof IFeatureInfoRecord)) {
      return false;
    } else {
      return evaluate((IFeatureInfoRecord) object);
    }
  }

  public abstract boolean evaluate(IFeatureInfoRecord featureInfoRecord);

  protected static boolean isTypeUse(IFeatureInfoRecord featureInfoRecord) {
    return (featureInfoRecord.getFeatureInfoID().getFeatureType() == IFeatureInfoId.FeatureType.TYPEREF) &&
           (featureInfoRecord.getDefUse() == IFeatureInfoRecord.DefUse.READ);
  }
}
