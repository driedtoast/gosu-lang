package gw.lang.tidb;

import gw.util.Predicate;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public class AllFeatureInfoRecordsPredicate implements Predicate
{
  public static final Predicate INSTANCE = new AllFeatureInfoRecordsPredicate();

  private AllFeatureInfoRecordsPredicate() {
  }

  public boolean evaluate(Object object) {
    return true;
  }
}
