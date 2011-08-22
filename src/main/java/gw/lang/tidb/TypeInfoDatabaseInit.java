package gw.lang.tidb;

import gw.util.IStagedProgressCallback;
import gw.lang.parser.IParsedElement;
import gw.config.CommonServices;

import java.util.concurrent.ThreadPoolExecutor;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public class TypeInfoDatabaseInit
{
  
  public static boolean initializeForProduction( String databaseDirLocation, IStagedProgressCallback progress )
  {
    return CommonServices.getTypeInfoDatabaseInit().initializeForProduction( databaseDirLocation, progress );
  }

  public static void shutdown()
  {
    CommonServices.getTypeInfoDatabaseInit().shutdown();
  }

  public static ThreadPoolExecutor getExecutor()
  {
    return CommonServices.getTypeInfoDatabaseInit().getExecutor();
  }

  public static boolean isInitialized()
  {
    return CommonServices.getTypeInfoDatabaseInit().isInitialized();
  }

  public static FeatureInfoRecordFinder getFeatureInfoRecordFinder()
  {
    return CommonServices.getTypeInfoDatabaseInit().getFeatureInfoRecordFinder();
  }

  public static InheritanceInfoRecordFinder getInheritenceInfoRecordFinder()
  {
    return CommonServices.getTypeInfoDatabaseInit().getInheritenceInfoRecordFinder();
  }

  public static FeatureInfoRecordFinder getFeatureInfoRecordFinderForLocalAndPersistedSources( String fullyQualifiedTypeName )
  {
    return CommonServices.getTypeInfoDatabaseInit().getFeatureInfoRecordFinderForLocalAndPersistedSources( fullyQualifiedTypeName );
  }

  public static FeatureInfoRecordFinder getFeatureInfoRecordFinderForAllLocalFeatures( String fullyQualifiedTypeName )
  {
    return CommonServices.getTypeInfoDatabaseInit().getFeatureInfoRecordFinderForAllLocalFeatures( fullyQualifiedTypeName );
  }

  public static FeatureInfoRecordFinder getFeatureInfoRecordFinderForAllLocalFeatures( IParsedElement parsedElement )
  {
    return CommonServices.getTypeInfoDatabaseInit().getFeatureInfoRecordFinderForAllLocalFeatures( parsedElement );
  }

  public static SourceDiffHandler getSourceDiffHandler()
  {
    return CommonServices.getTypeInfoDatabaseInit().getSourceDiffHandler();
  }

  public static void setDiffErrantTypesOnFinderUse( boolean diffErrantTypes )
  {
    CommonServices.getTypeInfoDatabaseInit().setDiffErrantTypesOnFinderUse( diffErrantTypes );
  }

  public static void setInitializationDone( boolean b )
  {
    CommonServices.getTypeInfoDatabaseInit().setInitializationDone( b );
  }

  public static String constructDatabaseURL( String databaseDirLocation )
  {
    return CommonServices.getTypeInfoDatabaseInit().constructDatabaseURL( databaseDirLocation );
  }

  public static void setFeatureInfoRecordFinder( FeatureInfoRecordFinder featureInfoRecordFinder )
  {
    CommonServices.getTypeInfoDatabaseInit().setFeatureInfoRecordFinder( featureInfoRecordFinder );
  }

  public static void setInheritanceInfoRecordFinder( InheritanceInfoRecordFinder featureInfoRecordFinder )
  {
    CommonServices.getTypeInfoDatabaseInit().setInheritanceInfoRecordFinder( featureInfoRecordFinder );
  }

  public static void runWithoutDiffing( Runnable r ) {
      CommonServices.getTypeInfoDatabaseInit().runWithoutDiffing( r );
  }

  public static boolean isTIDBDisabled()
  {
    String disable = System.getProperty( "disable.tidb" );
    return ! ( disable == null || disable.equals( "false" ) );
  }
}
