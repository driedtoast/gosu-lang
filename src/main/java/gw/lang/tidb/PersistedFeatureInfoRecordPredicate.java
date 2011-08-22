package gw.lang.tidb;

import gw.util.Predicate;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public class PersistedFeatureInfoRecordPredicate implements Predicate
{
  public static final Predicate INSTANCE = new PersistedFeatureInfoRecordPredicate();

  private PersistedFeatureInfoRecordPredicate() {
  }

  public final boolean evaluate(Object object) {
    if (!(object instanceof IFeatureInfoRecord)) {
      return false;
    } else {
      IFeatureInfoRecord record = (IFeatureInfoRecord) object;
      return (record.getFeatureInfoID().getFeatureType() != IFeatureInfoId.FeatureType.LOCAL);
    }
  }
}
