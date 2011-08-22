package gw.lang.tidb;

import java.sql.SQLException;
import java.util.Set;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface IFeatureInfoDbHandler
{
  int getNumIndices();

  IFeatureInfoId findFeatureInfoId(String fqnFeatureName, IFeatureInfoId.FeatureType type) throws SQLException;

  IFeatureInfoId findFeatureInfoId(Integer id) throws SQLException;

  Set<? extends IFeatureInfoId> findFeatureInfoIds(String fqnRelativeName) throws SQLException;

  int getLastFeatureInfoID();

  boolean add(Set<IFeatureInfoId> records);

  void remove(Set<IFeatureInfoId> records);
}
