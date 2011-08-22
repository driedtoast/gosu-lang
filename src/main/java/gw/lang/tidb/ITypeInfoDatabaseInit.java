package gw.lang.tidb;

import gw.config.IService;
import gw.lang.parser.IParsedElement;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeSystemRefresher;
import gw.util.IStagedProgressCallback;
import gw.util.fingerprint.FP64;

import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface ITypeInfoDatabaseInit extends IService
{
  public static final String DATABASE_NAME = "studio";
  public static final int SOURCE_DIFF_THREAD_PRIORITY = Thread.MIN_PRIORITY + 1;

  /**
   * In production, expected to be called only once.
   */
  public boolean initializeForProduction(String databaseDirLocation, final IStagedProgressCallback progress);

  public void shutdown();

  public ThreadPoolExecutor getExecutor();

  public boolean isInitialized();

  public FeatureInfoRecordFinder getFeatureInfoRecordFinder();

  public InheritanceInfoRecordFinder getInheritenceInfoRecordFinder();

  public FeatureInfoRecordFinder getFeatureInfoRecordFinderForLocalAndPersistedSources(String fullyQualifiedTypeName);

  public FeatureInfoRecordFinder getFeatureInfoRecordFinderForAllLocalFeatures(String fullyQualifiedTypeName);

  public FeatureInfoRecordFinder getFeatureInfoRecordFinderForAllLocalFeatures( IParsedElement parsedElement);

  public SourceDiffHandler getSourceDiffHandler();

  void setDiffErrantTypesOnFinderUse( boolean diffErrantTypes );

  void setLastTypeNameQueried( CharSequence lastTypeNameQueried );

  void setLastFeatureInfoRecords( Set<IFeatureInfoRecord> o );

  void setInitializationDone( boolean b );

  String constructDatabaseURL( String databaseDirLocation );

  void setFeatureInfoRecordFinder( FeatureInfoRecordFinder featureInfoRecordFinder );

  void setInheritanceInfoRecordFinder( InheritanceInfoRecordFinder featureInfoRecordFinder );

  FeatureInfoRecordFinder createFeatureInfoRecordFinder( ITypeInfoDatabase database );

  InheritanceInfoRecordFinder createInheritanceInfoRecordFinder( ITypeInfoDatabase database );

  ITypeInfoFingerprintRecord createTypeInfoFingerprintRecord( IFeatureInfoId iFeatureInfoId, FP64 fingerprint, boolean b );

  IInheritanceRecord createInheritanceRecord( IType supertype, IType type );
  IInheritanceRecord createInheritanceRecord( IFeatureInfoId root, IFeatureInfoId typeB );

    void runWithoutDiffing( Runnable r );

  void setTypeSystemRefresher(ITypeSystemRefresher refresher);

  ITypeSystemRefresher getTypeSystemRefresher();
  
}

