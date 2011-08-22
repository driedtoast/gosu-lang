package gw.lang.parser;

import gw.lang.parser.resources.ResourceKey;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.module.IModule;
import gw.lang.tidb.IFeatureInfoRecord;
import gw.util.Predicate;

import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IParsedElement
{
  void addExceptionsFrom( IParsedElement elem );

  IParseTree getLocation();
  void setLocation( IParseTree location );

  boolean hasParseIssues();

  List<IParseIssue> getParseIssues();

  boolean hasParseExceptions();
  boolean hasParseException( ResourceKey errKey );

  List<IParseIssue> getParseExceptions();

  void addParseException( ResourceKey msgKey, Object... args );
  void addParseException( IParseIssue e );

  void addParseWarning( ResourceKey msgKey, Object... args );
  void addParseWarning( IParseIssue warning );
  boolean hasParseWarning( ResourceKey errKey );

  void clearParseExceptions();

  void clearParseWarnings();

  boolean hasImmediateParseWarnings();

  boolean hasParseWarnings();

  List<IParseIssue> getParseWarnings();

  boolean hasParseIssue( IParseIssue pi );

  @SuppressWarnings("unchecked" )
  <E extends IParsedElement> boolean getContainedParsedElementsByType( Class<E> parsedElementType, List<E> listResults );

  boolean getContainedParsedElementsByTypes( List<IParsedElement> listResults, Class<? extends IParsedElement>... parsedElementTypes );

  boolean getContainedParsedElementsByTypesWithIgnoreSet( List<IParsedElement> listResults, Set<Class<? extends IParsedElement>> ignoreSet, Class<? extends IParsedElement>... parsedElementTypes );

  IType getReturnType();

  void clearParseTreeInformation();

  IParsedElement getParent();

  void setParent( IParsedElement rootElement );

  int getLineNum();

  int getColumn();

  boolean isSynthetic();

  String getFunctionName();

  String getModuleName();

  String getModuleNameRaw();
  
  IModule getModule();

  IManagedContext getManagedContext();

  IParsedElement findRootParsedElement();

  IParsedElement findAncestorParsedElementByType( Class... parsedElementClasses );

  void addFeatureInfoRecords( Set<IFeatureInfoRecord> featureInfoRecords, Stack<IFeatureInfo> enclosingFeatureInfos );

  Set<? extends IFeatureInfoRecord> getFeatureInfoRecords( Predicate predicate );

  IParsedElementWithAtLeastOneDeclaration findDeclaringStatement( IParsedElement parsedElement, CaseInsensitiveCharSequence identifierName );

  void performUnusedElementAnalysis( IParsedElement... parsedElements );

  String getFileName();
  
  <R> R dispatch(ParsedElementDispatch<R> dispatch);

  IFeatureInfo findQualifyingFeatureInfo( CaseInsensitiveCharSequence identifierName );

  IGosuProgram getGosuProgram();
}
