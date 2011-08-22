package gw.lang.tidb;

import gw.lang.reflect.IType;
import gw.util.Predicate;

import java.util.Set;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface ITypeInfoDatabaseIndexableType extends IType
{
  /**
   * @return The set of FeatureInfoRecords. Any symbols used in this ParsedElement and its "children"
   *         are added to this set. Only FeatureInfoRecords that match the given predicate are added.
   */
  public Set<? extends IFeatureInfoRecord> getFeatureInfoRecords( Predicate predicate );

  public void dropInfoForVerification();
}
