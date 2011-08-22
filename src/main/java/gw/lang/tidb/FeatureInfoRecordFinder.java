package gw.lang.tidb;

import gw.lang.parser.IParsedElement;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.IFieldAccessExpression;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.ILocalVarDeclaration;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.parser.expressions.IQueryPathExpression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.reflect.IFeatureInfo;

import java.util.Set;


/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface FeatureInfoRecordFinder {
  /**
   * @return Set of FeatureInfoRecords representing usages of the given element. Returns null if it is not
   * meaningful to find usages for the given ParsedElement
   */
  public Set<IFeatureInfoRecord> findUsages( IParsedElement parsedElement);
  public Set<IFeatureInfoRecord> findUsages( IIdentifierExpression identifier);
  public Set<IFeatureInfoRecord> findUsages( IFieldAccessExpression memberAccess);
  public Set<IFeatureInfoRecord> findUsages( ILocalVarDeclaration parameterDeclaration);
  public Set<IFeatureInfoRecord> findUsages( IMethodCallExpression methodCallExpression);
  public Set<IFeatureInfoRecord> findUsages( IBeanMethodCallExpression beanMethodCallExpression);
  public Set<IFeatureInfoRecord> findUsages( INewExpression newExpression);
  public Set<IFeatureInfoRecord> findUsages( IParsedElementWithAtLeastOneDeclaration statement);
  public Set<IFeatureInfoRecord> findUsages( IFeatureInfoRecord featureInfoRecord);

  /**
   * @return Set of FeatureInfoRecords representing usages of the GosuClass declared by the given statement.
   */
  public Set<IFeatureInfoRecord> findUsages( IClassStatement classStatement);

  /**
   * @return Set of FeatureInfoRecords representing usages of the type in the given ITypeLiteralExpression.
   */
  public Set<IFeatureInfoRecord> findUsages( ITypeLiteralExpression typeLiteral);

  /**
   * @return Set of FeatureInfoRecords representing usages of the given feature info
   */
  public Set<IFeatureInfoRecord> findUsages( IFeatureInfo featureInfo);

  /**
   * @return Set of FeatureInfoRecords for declarations of functions that override the one declared by the given
   * FunctionStatement
   */
  public Set<IFeatureInfoRecord> findMethodsThatOverrideThis( IFunctionStatement functionStatement);

  /**
   * @return Set of FeatureInfoRecords for declarations of all the functions that override the method in the given
   * member access. Returns an empty set if there are no such methods, or if the identifier is not part of a
   * BeanMethodCallExpression
   */
  public Set<IFeatureInfoRecord> findMethodsThatOverrideThis( IBeanMethodCallExpression beanMethodCall);

  /**
   * @return Set of FeatureInfoRecords for declarations of all the functions that override the method in the given
   * MethodCallExpression. Returns an empty set if there are no such methods.
   */
  public Set<IFeatureInfoRecord> findMethodsThatOverrideThis( IMethodCallExpression methodCallExpression);

  /**
   * @return Set of FeatureInfoRecords for declarations of all the functions that override the given method.
   * Returns an empty set if there are no such methods.
   */
  public Set<IFeatureInfoRecord> findMethodsThatOverrideThis( IFeatureInfo methodOrConstructorInfo);

  /**
   * @return The IFeatureInfoRecord for the declaration of the function that is overridden by the one
   * declared by the given FunctionStatement. Returns null if there is no such method.
   */
  public IFeatureInfoRecord findMethodThatIsOverriddenByThis( IFunctionStatement functionStatement);

  /**
   * @return Set of FeatureInfoRecords whose qualifying enclosing type info is the given type. The version that takes a
   * FeatureInfoID can be used when the type is not available, e.g. it has been deleted.
   */
  public Set<IFeatureInfoRecord> findRecordsInSource( IFeatureInfoId typeInfoID);

  /**
   * @return Set of FeatureInfoRecords enclosed by the given record, i.e. those whose enclosing type info and
   * qualifying enclosing type info are the same as the feature info and owning type info of the given record.
   */
  public Set<IFeatureInfoRecord> findEnclosedRecords(IFeatureInfo enclosingFeatureInfo);
  public Set<IFeatureInfoRecord> findEnclosedRecords( IFeatureInfoId enclosingFeatureInfoID, IFeatureInfoId qualifyingEnclosingFeatureInfoID);

  public Set<IFeatureInfoRecord> findOwnedRecords(IFeatureInfo enclosingFeatureInfo);

  /**
   * @return IFeatureInfoRecord for the declaration of the element being used in the given expression. Returns null if
   * it is not meaningful to find a declaration for the given ParsedElement.
   * @throws IllegalStateException if the declaration of the element could not be found
   */
  public IFeatureInfoRecord findDeclaration( IParsedElement parsedElement);
  public IFeatureInfoRecord findDeclaration( IIdentifierExpression identifier);
  public IFeatureInfoRecord findDeclaration( IFieldAccessExpression memberAccess);
  public IFeatureInfoRecord findDeclaration( ILocalVarDeclaration parameterDeclaration);
  public IFeatureInfoRecord findDeclaration( IMethodCallExpression methodCallExpression);
  public IFeatureInfoRecord findDeclaration( IBeanMethodCallExpression beanMethodCallExpression);
  public IFeatureInfoRecord findDeclaration( IQueryPathExpression queryPathExpression);
  public IFeatureInfoRecord findDeclaration( INewExpression newExpression);
  public IFeatureInfoRecord findDeclaration(IFeatureInfo featureInfo);
  public IFeatureInfoRecord findDeclaration(IFeatureInfoRecord featureInfoRecord);
  public IFeatureInfoRecord findDeclaration(String className, String methodName);

  /**
   * @return IFeatureInfoRecord for the declaration of the type being used in the given ITypeLiteralExpression.
   */
  public IFeatureInfoRecord findDeclaration( ITypeLiteralExpression typeLiteral);

  public ITypeInfoFingerprintRecord findFingerprint( IFeatureInfoId typeInfoID);

  /**
   * @return Fingerprint records for types that are affected by a prior edit. We do not run the source diff on these types.
   * Instead we do it when we use the type info database or once at startup.
   */
  public Set<ITypeInfoFingerprintRecord> findTypesThatNeedToBeDiffed();

  /**
   * @return Fingerprint records for types that are known to have errors
   */
  public Set<ITypeInfoFingerprintRecord> findErrantTypes();

  /**
   * @return Set of FeatureInfoRecords that match the given name. May contain % for LIKE queries
   */
  public Set<IFeatureInfoRecord> findByName(String featureName);
}
