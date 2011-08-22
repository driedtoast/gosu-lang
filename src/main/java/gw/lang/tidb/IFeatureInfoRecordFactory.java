package gw.lang.tidb;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IType;
import gw.lang.parser.IParsedElement;
import gw.util.fingerprint.FP64;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface IFeatureInfoRecordFactory
{
  IFeatureInfoRecord create( IFeatureInfo featureInfo,
                           IFeatureInfo owningFeatureInfo,
                           IFeatureInfo enclosingFeatureInfo,
                           ITypeInfo qualifyingEnclosingFeatureInfo,
                           int offSetOfDefinition,
                           int lengthOfRecord,
                           int lineNumber,
                           int columnNumber,
                           IFeatureInfoRecord.DefUse defUse,
                           IParsedElement originator);

  IFeatureInfoRecord create( IFeatureInfoId IFeatureInfoId,
                           IFeatureInfo owningFeatureInfo,
                           IFeatureInfo enclosingFeatureInfo,
                           ITypeInfo qualifyingEnclosingFeatureInfo,
                           int offSetOfDefinition,
                           int lengthOfRecord,
                           int lineNumber,
                           int columnNumber,
                           IFeatureInfoRecord.DefUse defUse,
                           IParsedElement originator);

  IFeatureInfoRecord create( IFeatureInfoId IFeatureInfoId,
                           IFeatureInfoId owningIFeatureInfoId,
                           IFeatureInfoId enclosingIFeatureInfoId,
                           IFeatureInfoId qualifyingEnclosingIFeatureInfoId,
                           IFeatureInfoRecord.DefUse defUse,
                           IParsedElement originator);

  IFeatureInfoRecord create( IFeatureInfoId IFeatureInfoId,
                           IFeatureInfoId owningIFeatureInfoId,
                           IFeatureInfoId enclosingIFeatureInfoId,
                           IFeatureInfoId qualifyingEnclosingIFeatureInfoId,
                           int offSetOfDefinition,
                           int lengthOfRecord,
                           int lineNumber,
                           int columnNumber,
                           IFeatureInfoRecord.DefUse defUse,
                           IParsedElement originator);

  IFeatureInfoRecord create( int rowNum,
                           IFeatureInfoId IFeatureInfoId,
                           IFeatureInfoId owningIFeatureInfoId,
                           IFeatureInfoId enclosingIFeatureInfoId,
                           IFeatureInfoId qualifyingEnclosingIFeatureInfoId,
                           int offSetOfDefinition,
                           int lengthOfRecord,
                           int lineNumber,
                           int columnNumber,
                           IFeatureInfoRecord.DefUse defUse,
                           IParsedElement originator);

  public IFeatureInfoId generateID(IFeatureInfo featureInfo);

  public IFeatureInfoId generateID( IType type);

  public IFeatureInfoId generateID(ITypeInfo typeInfo);

  public IFeatureInfoId generateID(IPropertyInfo propertyInfo);

  public IFeatureInfoId generateID(IMethodInfo methodInfo);

  public IFeatureInfoId generateID(IConstructorInfo constructorInfo);

  IFeatureInfoId createID( IFeatureInfoId.FeatureType type, String strType );

  ITypeInfoFingerprintRecord createTypeInfoFingerprintRecord(IFeatureInfoId id, FP64 fp64, boolean needsToBeDiffed, boolean errant);
}
