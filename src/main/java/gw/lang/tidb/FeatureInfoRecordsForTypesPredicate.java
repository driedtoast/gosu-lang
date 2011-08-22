package gw.lang.tidb;

import gw.util.Predicate;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public class FeatureInfoRecordsForTypesPredicate extends AbstractFeatureInfoRecordsPredicate
{
  public static final Predicate INSTANCE = new FeatureInfoRecordsForTypesPredicate();

  private FeatureInfoRecordsForTypesPredicate() {
  }

  public boolean evaluate(IFeatureInfoRecord featureInfoRecord) {
    return isTypeUse(featureInfoRecord);
  }
}
