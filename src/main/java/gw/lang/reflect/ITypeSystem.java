package gw.lang.reflect;

import gw.config.IService;
import gw.config.ResourceFileResolver;
import gw.lang.debugger.IDebugManager;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;
import gw.util.IProgressCallback;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITypeSystem extends IService
{
  /**
   * Gets the intrinsic type for a given class.<p>
   * <p/>
   * <b>Note:</b> you should use this method only if you do not have an
   * Object of class <code>javaClass</code> to get the type from. If you
   * do have such an object, use {@link #getFromObject} instead.
   *
   * @param javaClass the Class to convert to an intrinsic type
   *
   * @return the IType that corresponds to that class
   *
   * @see #getFromObject(Object)
   */
  IType get( Class<?> javaClass );

  /**
   * Gets the intrinsic type for a given class info object.<p>
   *
   * @param javaClassInfo the Class info object to convert to an intrinsic type
   *
   * @return the IType that corresponds to that class
   */
  IType get(IJavaClassInfo javaClassInfo);

  /**
   * Returns the intrinsic type for the given Object.
   *
   * @param object the object to get an IType for
   *
   * @return the IType for the object
   *
   * @see #get(Class)
   */
  IType getFromObject( Object object );

  IType getByRelativeName( String relativeName ) throws ClassNotFoundException;

  /**
   * Gets an intrinsic type based on a relative name.  This could either be the name of an entity,
   * like "User", the name of a typekey, like "SystemPermission", or a class name, like
   * "java.lang.String" (relative and fully qualified class names are the same as far as this factory
   * is concerned).  Names can have [] appended to them to create arrays, and multi-dimensional arrays
   * are supported.
   *
   * @param relativeName the relative name of the type
   * @param typeUses     the map of used types to use when resolving
   *
   * @return the corresponding IType
   *
   * @throws ClassNotFoundException if the specified name doesn't correspond to any type
   */
  IType getByRelativeName( String relativeName, ITypeUsesMap typeUses ) throws ClassNotFoundException;

  /**
   * Gets an intrinsic type based on a fully-qualified name.  This could either be the name of an entity,
   * like "entity.User", the name of a typekey, like "typekey.SystemPermission", or a class name, like
   * "java.lang.String".  Names can have [] appended to them to create arrays, and multi-dimensional arrays
   * are supported.
   *
   * @param fullyQualifiedName the fully qualified name of the type
   *
   * @return the corresponding IType
   *
   * @throws RuntimeException if the specified name doesn't correspond to any type
   */
  IType getByFullName( String fullyQualifiedName );

  /**
   * Gets a type based on a fully-qualified name.  This could either be the name of an entity,
   * like "entity.User", the name of a typekey, like "typekey.SystemPermission", or a class name, like
   * "java.lang.String".  Names can have [] appended to them to create arrays, and multi-dimensional arrays
   * are supported.
   *
   * This method behaves the same as getByFullName execept instead of throwing it returns null.
   *
   * @param fullyQualifiedName the fully qualified name of the type
   *
   * @return the corresponding IType or null if the type does not exist
   */
  IType getByFullNameIfValid( String fullyQualifiedName );

  void refresh(ITypeRef typeRef, boolean fireChangeEvent);
  void refresh();
  void refresh( boolean bRefreshCaches );
  void refresh(IModule module, boolean bRefreshCaches);

  int getRefreshChecksum();
  int getSingleRefreshChecksum();

  /**
   * Converts a String name of a type into an IType.
   *
   * @throws IllegalArgumentException if the type string doesn't correspond to any known IType
   */
  IType parseType( String typeString ) throws IllegalArgumentException;

  IType parseType( String typeString, ITypeUsesMap typeUsesMap ) throws IllegalArgumentException;

  IType parseType( String typeString, TypeVarToTypeMap actualParamByVarName );

  ITypeLiteralExpression parseTypeExpression( String typeString, TypeVarToTypeMap actualParamByVarName, ITypeUsesMap typeUsesMap ) throws ParseResultsException;

  /**
   * Aquires the global type-system lock
   */
  void lock();

  /**
   * Releases the global type-sytem lock
   */
  void unlock();

  Lock getGlobalLock();

  IType getComponentType( IType valueType );

  /**
   * @return true if the type exists, false otherwise
   */
  boolean exists( String fullyQualifiedClassName );

  INamespaceType getNamespace( String strFqNamespace );


  /**
   * Returns all type names in the system for all type loaders.
   * @return all type names in the system.
   */
  Set<? extends CharSequence> getAllTypeNames();


  ITypeVariableType getOrCreateTypeVariableType( String strName, IType boundingType, IType enclosingType );

  IFunctionType getOrCreateFunctionType( IMethodInfo mi );
  IFunctionType getOrCreateFunctionType( String strFunctionName, IType retType, IType[] paramTypes );

  TypeVarToTypeMap mapTypeByVarName( IType ownersType, IType declaringType, boolean bKeepTypeVars );

  IType getActualType( IType type, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars );

  void inferTypeVariableTypesFromGenParamTypeAndConcreteType( IType genParamType, IType argType, TypeVarToTypeMap map );

  IType findLeastUpperBound( List<? extends IType> types );

  IErrorType getErrorType();
  IErrorType getErrorType( String strErrantName );
  IErrorType getErrorType( ParseResultsException pe );

  Set<IType> getAllClassesInClassHierarchyAsIntrinsicTypes( Class entityClass );

  IDefaultTypeLoader getDefaultTypeLoader();

  IType findParameterizedType( IType type, IType rhsType );

  void addTypeLoaderListenerAsWeakRef( ITypeLoaderListener listener );

  Set<String> getNamespacesFromTypeNames( Set<? extends CharSequence> allTypeNames, Set<String> namespaces );

  void pushGlobalTypeLoader( ITypeLoader loader );
  void pushGlobalTypeLoader( IModule module, ITypeLoader loader );
  void removeGlobalTypeLoader( Class<? extends ITypeLoader> loader );

  Class getClassFromName( String strFullName ) throws ClassNotFoundException;

  void notifyOfTypeRefresh( IType type );
  void notifyOfTypeCreation( String strType );
  void notifyOfTypeDeletion( String strType );

  boolean areBeansEqual( Object o1, Object o2 );

  void pushIncludeAll();
  void popIncludeAll();
  boolean isIncludeAll();

  IType getCurrentCompilingType();
  IType getCompilingType( String strName );
  void pushCompilingType(IType type);
  void popCompilingType();

  void pushOuterDebugManager( IDebugManager debugManager );
  void popOuterDebugManager();

  void pushSymTableCtx( ISymbolTable ctx );
  void popSymTableCtx();
  ISymbolTable getSymTableCtx();

  void addNamespaceFromFqnToSet( Set<String> allNamespaces, String strFqn );

  <T extends ITypeLoader> T getTypeLoader( Class<? extends T> loaderClass );

  <T extends ITypeLoader> T getTypeLoader( Class<? extends T> loaderClass, IModule module );

  String getNameOfParams( IType[] paramTypes, boolean bRelative, boolean bWithEnclosingType );

  ISymbolTable getCompiledGosuClassSymbolTable();

  void clearNameCaches();

  List<ITypeLoader> getGlobalTypeLoadersForTesting();

  void removeAllTypeLoaders();

  List<CharSequence> getFullyQualifiedClassNameFromRelativeName( String strRelativeName );

  Set<CharSequence> getAllIdeLoadableTypeNames( IProgressCallback progress, boolean bInternalMode );

  List<Throwable> getTypeLoaderErrors();

  Collection<? extends String> getAllHandledPrefixes();

  IType getJavaType(Class javaClass);

  String getNameWithQualifiedTypeVariables(IType type);

  IType getDefaultParameterizedType(IType type);

  boolean canCast(IType lhsType, IType rhsType);

  void removeTypeLoaderListener(ITypeLoaderListener listener);

  IJavaType getPrimitiveType(String name);

  IType getPrimitiveType(IType boxType);

  IType getBoxType(IType primitiveType);
  
  IExecutionEnvironment getExecutionEnvironment();

  IModule getCurrentModule();

  void setResourceFileResolver(ResourceFileResolver resolver);

  ITypeRef getOrCreateTypeReference( IType type );

  Class<? extends ITypeRef> getOrCreateTypeClassProxy( String strTypeClass );

  ITypeRef getTypeReference( IType type );

  void refreshAndClearCaches();

  URL getResource( String strResourceName );

  ResourceFileResolver getResourceFileResolver();

  ITypeLoader getTypeLoader( IGosuClass loaderClass );

  IType getTypeFromObject( Object obj );

  boolean isExpandable( IType type );

  IType getExpandableComponentType( IType type );

  void clearErrorTypes();

  IType boundTypes(IType parameterType, List<IType> inferringTypes);

  IJavaClassInfo getDefaultJavaClassInfo(Class jClass);

  ICompoundType getCompoundType(IType... componentTypes);

  ICompoundType getCompoundType(Iterable<? extends IType> componentTypes);
}
