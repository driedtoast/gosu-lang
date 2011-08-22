package gw.lang.tidb;

import gw.util.Predicate;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public class FeatureInfoRecordsForSymbolsPredicate extends AbstractFeatureInfoRecordsPredicate {
  public static final Predicate INSTANCE = new FeatureInfoRecordsForSymbolsPredicate();

  private FeatureInfoRecordsForSymbolsPredicate() {
  }

  public boolean evaluate(IFeatureInfoRecord featureInfoRecord) {
    return !AbstractFeatureInfoRecordsPredicate.isTypeUse(featureInfoRecord);
  }
}
