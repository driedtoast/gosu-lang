package gw.lang.tidb;

import gw.util.IProgressCallback;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface IFeatureInfoRecordDbHandler
{
  Set<IFeatureInfoRecord> findDeclarationsOnTypes( IFeatureInfoId featureInfoID, Set<IFeatureInfoId> owningTypes);// Called from the editor to find all usages of a type in all other files.  Probably wrong

  Set<IFeatureInfoRecord> findUsages( IFeatureInfoId mainFeatureInfoID, Set<IFeatureInfoId> owningTypes, List<IFeatureInfoId> additionalFeaturesToSearch);// Called by the editor in the source file diff handler

  Set<IFeatureInfoRecord> findRecordsInSource( IFeatureInfoId typeInfoID);

  Set<IFeatureInfoRecord> findEnclosedRecords( IFeatureInfoId enclosingFeatureInfoID, IFeatureInfoId qualifyingEnclosingFeatureInfoID);

  Set<IFeatureInfoRecord> findOwnedRecords( IFeatureInfoId owningFeatureInfoID);

  IFeatureInfoRecord findDeclarations( IFeatureInfoId owningType, Set<IFeatureInfoId> featuresToFind);// Very slow query, used lazily when an operation is invoked on the type info DB.

  Set<IFeatureInfoRecord> findDeclByFeatureID(Set<IFeatureInfoId> featureIDs);

  int getNumIndices();// Called to update the line numbers from the editor
  public void createIndices(IProgressCallback progress);

  boolean updatePosition(Set<? extends IFeatureInfoRecordPositionUpdate> records) throws SQLException;// Adds a new FIR

  boolean add(Set<IFeatureInfoRecord> records);// Removes FIRs
  void remove(Set<IFeatureInfoRecord> records);

  public void createTable();
  public void dropTable();
  public boolean doesTableMatch();

  public void startup(String dbName);
  public void cleanup();

  void removeRecordsForTypes(Set<ITypeInfoFingerprintRecord> records);
}
