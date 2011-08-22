package gw.lang.reflect;

import gw.config.CommonServices;
import gw.config.ResourceFileResolver;
import gw.lang.UnstableAPI;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.gs.IGosuArrayClass;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.debugger.IDebugManager;
import gw.lang.reflect.gs.IGosuClassLoader;
import gw.lang.reflect.java.IJavaArrayType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.module.ModuleClassLoader;
import gw.util.IFeatureFilter;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IParserPart;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.util.IProgressCallback;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class TypeSystem
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
  public static IType get( Class javaClass )
  {
    return CommonServices.getTypeSystem().get( javaClass );
  }

  public static IType get(IJavaClassInfo javaClassInfo) {
    return CommonServices.getTypeSystem().get(javaClassInfo);
  }

  /**
   * Returns the intrinsic type for the given Object.
   *
   * @param object the object to get an IType for
   *
   * @return the IType for the object
   *
   * @see #get(Class)
   */
  public static IType getFromObject( Object object )
  {
    return CommonServices.getTypeSystem().getFromObject( object );
  }

  public static IType getByRelativeName( String relativeName ) throws ClassNotFoundException
  {
    return CommonServices.getTypeSystem().getByRelativeName( relativeName );
  }

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
  public static IType getByRelativeName( String relativeName, ITypeUsesMap typeUses ) throws ClassNotFoundException
  {
    return CommonServices.getTypeSystem().getByRelativeName( relativeName, typeUses );
  }

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
  public static IType getByFullName( String fullyQualifiedName )
  {
    return CommonServices.getTypeSystem().getByFullName( fullyQualifiedName );
  }

  public static IType getByFullName( String fullyQualifiedName, String moduleName )
  {
    IModule module = moduleName == null ? getExecutionEnvironment().getJreModule() : getExecutionEnvironment().getModule(moduleName);
    getExecutionEnvironment().pushModule(module);
    try {
      return CommonServices.getTypeSystem().getByFullName( fullyQualifiedName );
    } finally {
      getExecutionEnvironment().popModule(module);
    }
  }

  public static URL getResource( String strResourceName )
  {
    return CommonServices.getTypeSystem().getResource( strResourceName );
  }

  public static ResourceFileResolver getResourceFileResolver()
  {
    return CommonServices.getTypeSystem().getResourceFileResolver();
  }

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
  public static IType getByFullNameIfValid( String fullyQualifiedName )
  {
    return CommonServices.getTypeSystem().getByFullNameIfValid( fullyQualifiedName );
  }

  public static void clearErrorTypes()
  {
    CommonServices.getTypeSystem().clearErrorTypes();
  }

  /**
   * Refresh just the specified type i.e., a gosu editor calls this on changes
   */
  public static void refresh(ITypeRef typeRef, boolean fireChangeEvent)
  {
    CommonServices.getTypeSystem().refresh( typeRef, fireChangeEvent );
  }

  public static void refresh()
  {
    CommonServices.getTypeSystem().refresh();
  }

  public static void refresh( boolean bRefreshCaches )
  {
    CommonServices.getTypeSystem().refresh( bRefreshCaches );
  }

  public static void refresh( IModule module, boolean bRefreshCaches )
  {
    CommonServices.getTypeSystem().refresh( module, bRefreshCaches );
  }

  public static int getRefreshChecksum()
  {
    return CommonServices.getTypeSystem().getRefreshChecksum();
  }

  public static int getSingleRefreshChecksum()
  {
    return CommonServices.getTypeSystem().getSingleRefreshChecksum();
  }

  /**
   * Converts a String name of a type into an IType.
   *
   * @param typeString the type name to parse
   * @return the parsed type
   * @throws IllegalArgumentException if the type string doesn't correspond to any known IType
   */
  public static IType parseType( String typeString ) throws IllegalArgumentException
  {
    return CommonServices.getTypeSystem().parseType( typeString );
  }

  public static IType parseType( String typeString, ITypeUsesMap typeUsesMap ) throws IllegalArgumentException
  {
    return CommonServices.getTypeSystem().parseType( typeString, typeUsesMap );
  }

  public static IType parseType( String typeString,  TypeVarToTypeMap actualParamByVarName ) throws IllegalArgumentException
  {
    return CommonServices.getTypeSystem().parseType( typeString, actualParamByVarName );
  }

  public static ITypeLiteralExpression parseTypeExpression( String typeString, TypeVarToTypeMap actualParamByVarName, ITypeUsesMap typeUsesMap ) throws ParseResultsException
  {
    return CommonServices.getTypeSystem().parseTypeExpression( typeString, actualParamByVarName, typeUsesMap );
  }

  /**
   * Acquires the global type-system lock
   */
  public static void lock()
  {
    CommonServices.getTypeSystem().lock();
  }

  /**
   * Releases the global type-system lock
   */
  public static void unlock()
  {
    CommonServices.getTypeSystem().unlock();
  }

  public static Lock getGlobalLock()
  {
    return CommonServices.getTypeSystem().getGlobalLock();
  }

  public static IType getComponentType( IType valueType )
  {
    return CommonServices.getTypeSystem().getComponentType( valueType );
  }

  /**
   * @param fullyQualifiedClassName the type name to check for existence
   * @return true if the type exists, false otherwise
   */
  public static boolean exists( String fullyQualifiedClassName )
  {
    return CommonServices.getTypeSystem().exists( fullyQualifiedClassName );
  }

  public static INamespaceType getNamespace( String strFqNamespace )
  {
    return CommonServices.getTypeSystem().getNamespace( strFqNamespace );
  }

  /**
   * Returns all type names in the system for all type loaders.
   * @return all type names in the system.
   */
  public static Set<? extends CharSequence> getAllTypeNames()
  {
    return CommonServices.getTypeSystem().getAllTypeNames();
  }

  public static ITypeVariableType getOrCreateTypeVariableType( String strName, IType boundingType, IType enclosingType )
  {
    return CommonServices.getTypeSystem().getOrCreateTypeVariableType( strName, boundingType, enclosingType );
  }

  public static IFunctionType getOrCreateFunctionType( IMethodInfo mi )
  {
    return CommonServices.getTypeSystem().getOrCreateFunctionType( mi );
  }
  public static IFunctionType getOrCreateFunctionType( String strFunctionName, IType retType, IType[] paramTypes )
  {
    return CommonServices.getTypeSystem().getOrCreateFunctionType( strFunctionName, retType, paramTypes );
  }

  public static <E extends IType> E getPureGenericType( E type )
  {
    while( type.isParameterizedType() )
    {
      //noinspection unchecked
      type = (E)type.getGenericType();
    }
    return type;
  }

  public static boolean isBeanType( IType typeSource )
  {
    return
      typeSource != IGosuParser.STRING_TYPE &&
      typeSource != IGosuParser.BOOLEAN_TYPE &&
     // typeSource != IGosuParser.DATETIME_TYPE &&
      typeSource != IGosuParser.NULL_TYPE &&
      typeSource != IGosuParser.NUMBER_TYPE &&
      !typeSource.isPrimitive() &&
      !typeSource.isArray() &&
      !(typeSource instanceof IFunctionType) &&
      !(typeSource instanceof IConstructorType) &&
      !(typeSource instanceof IMetaType);
  }

  public static boolean isNumericType( IType intrType )
  {
    return intrType != null && ((intrType.isPrimitive() &&
            intrType != IJavaType.pBOOLEAN &&
            intrType != IJavaType.pVOID) ||
            IJavaType.NUMBER.isAssignableFrom(intrType) ||
            IJavaType.IDIMENSION.isAssignableFrom(intrType) ||
            IJavaType.CHARACTER.isAssignableFrom(intrType));

  }

  public static boolean isBoxedTypeFor( IType primitiveType, IType boxedType )
  {
    if( primitiveType != null && primitiveType.isPrimitive() )
    {
      if( primitiveType == IJavaType.pBOOLEAN && boxedType == IJavaType.BOOLEAN )
      {
        return true;
      }
      if( primitiveType == IJavaType.pBYTE && boxedType == IJavaType.BYTE )
      {
        return true;
      }
      if( primitiveType == IJavaType.pCHAR && boxedType == IJavaType.CHARACTER )
      {
        return true;
      }
      if( primitiveType == IJavaType.pDOUBLE && boxedType == IJavaType.DOUBLE )
      {
        return true;
      }
      if( primitiveType == IJavaType.pFLOAT && boxedType == IJavaType.FLOAT )
      {
        return true;
      }
      if( primitiveType == IJavaType.pINT && boxedType == IJavaType.INTEGER )
      {
        return true;
      }
      if( primitiveType == IJavaType.pLONG && boxedType == IJavaType.LONG )
      {
        return true;
      }
      if( primitiveType == IJavaType.pSHORT && boxedType == IJavaType.SHORT )
      {
        return true;
      }
    }
    return false;
  }

  public static TypeVarToTypeMap mapTypeByVarName( IType ownersType, IType declaringType, boolean bKeepTypeVars )
  {
    return CommonServices.getTypeSystem().mapTypeByVarName( ownersType, declaringType, bKeepTypeVars );
  }

  public static IType getActualType( IType type, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars )
  {
    return CommonServices.getTypeSystem().getActualType( type, actualParamByVarName, bKeepTypeVars );
  }

  public static void inferTypeVariableTypesFromGenParamTypeAndConcreteType( IType genParamType, IType argType, TypeVarToTypeMap map )
  {
    CommonServices.getTypeSystem().inferTypeVariableTypesFromGenParamTypeAndConcreteType( genParamType, argType, map );
  }

  public static IType findLeastUpperBound( List<? extends IType> types )
  {
    return CommonServices.getTypeSystem().findLeastUpperBound( types );
  }

  public static IErrorType getErrorType()
  {
    return CommonServices.getTypeSystem().getErrorType();
  }
  public static IErrorType getErrorType( String strErrantName )
  {
    return CommonServices.getTypeSystem().getErrorType( strErrantName );
  }
  public static IErrorType getErrorType( ParseResultsException pe )
  {
    return CommonServices.getTypeSystem().getErrorType( pe );
  }

  public static Set<IType> getAllClassesInClassHierarchyAsIntrinsicTypes( Class entityClass )
  {
    return CommonServices.getTypeSystem().getAllClassesInClassHierarchyAsIntrinsicTypes( entityClass );
  }

  public static IDefaultTypeLoader getDefaultTypeLoader()
  {
    return CommonServices.getTypeSystem().getDefaultTypeLoader();
  }

  public static IType findParameterizedType( IType type, IType rhsType )
  {
    return CommonServices.getTypeSystem().findParameterizedType( type, rhsType );
  }

  public static void addTypeLoaderListenerAsWeakRef( ITypeLoaderListener listener )
  {
    CommonServices.getTypeSystem().addTypeLoaderListenerAsWeakRef( listener );
  }

  public static Set<String> getNamespacesFromTypeNames( Set<? extends CharSequence> allTypeNames, Set<String> namespaces )
  {
    return CommonServices.getTypeSystem().getNamespacesFromTypeNames( allTypeNames, namespaces );
  }

  public static void pushGlobalTypeLoader( ITypeLoader loader )
  {
    CommonServices.getTypeSystem().pushGlobalTypeLoader( loader );
  }
  public static void pushGlobalTypeLoader( IModule module, ITypeLoader loader )
  {
    CommonServices.getTypeSystem().pushGlobalTypeLoader( module, loader );
  }
  public static void removeGlobalTypeLoader( Class<? extends ITypeLoader> loader )
  {
    CommonServices.getTypeSystem().removeGlobalTypeLoader( loader );
  }

  public static Class getClassFromName( String type ) throws ClassNotFoundException
  {
    return CommonServices.getTypeSystem().getClassFromName( type );
  }

  public static void notifyOfTypeRefresh( IType type )
  {
    CommonServices.getTypeSystem().notifyOfTypeRefresh( type );
  }

  public static void notifyOfTypeCreation( String strName )
  {
    CommonServices.getTypeSystem().notifyOfTypeCreation( strName );
  }

  public static void notifyOfTypeDeletion( String strName )
  {
    CommonServices.getTypeSystem().notifyOfTypeDeletion( strName );
  }

  public static IType getKeyType()
  {
    return CommonServices.getEntityAccess().getKeyType();
  }

  public static boolean areBeansEqual( Object o1, Object o2 )
  {
    return CommonServices.getTypeSystem().areBeansEqual( o1, o2 );
  }

  public static void pushIncludeAll()
  {
    CommonServices.getTypeSystem().pushIncludeAll();
  }
  public static void popIncludeAll()
  {
    CommonServices.getTypeSystem().popIncludeAll();
  }
  public static boolean isIncludeAll()
  {
    return CommonServices.getTypeSystem().isIncludeAll();
  }

  public static ITypeUsesMap getDefaultTypeUsesMap()
  {
    return CommonServices.getEntityAccess().getDefaultTypeUses();
  }

  public static IType getCurrentCompilingType() {
    return CommonServices.getTypeSystem().getCurrentCompilingType();
  }

  public static IType getCompilingType( String strName )
  {
    return CommonServices.getTypeSystem().getCompilingType( strName );
  }

  public static void pushCompilingType(IType type) {
    CommonServices.getTypeSystem().pushCompilingType(type);
  }

  public static void popCompilingType() {
    CommonServices.getTypeSystem().popCompilingType();
  }

  public static String getUnqualifiedClassName( IType cls )
  {
    return cls == null ? "<null>" : cls.getRelativeName();
  }

  public static String getUnqualifiedClassName( String strQualifiedClassName )
  {
    int iDotIndex = strQualifiedClassName.lastIndexOf( '.' );
    return strQualifiedClassName.substring( iDotIndex + 1 );
  }

  public static void pushOuterDebugManager( IDebugManager debugManager )
  {
    CommonServices.getTypeSystem().pushOuterDebugManager( debugManager );
  }
  public static void popOuterDebugManager()
  {
    CommonServices.getTypeSystem().popOuterDebugManager();
  }

  public static void pushSymTableCtx( ISymbolTable ctx )
  {
    CommonServices.getTypeSystem().pushSymTableCtx( ctx );
  }
  public static void popSymTableCtx()
  {
    CommonServices.getTypeSystem().popSymTableCtx();
  }
  public static ISymbolTable getSymTableCtx()
  {
    return CommonServices.getTypeSystem().getSymTableCtx();
  }

  public static void addNamespaceFromFqnToSet( Set<String> allNamespaces, String strFqn )
  {
    CommonServices.getTypeSystem().addNamespaceFromFqnToSet( allNamespaces, strFqn );
  }

  public static <T extends ITypeLoader> T getTypeLoader( Class<? extends T> loaderClass )
  {
    return CommonServices.getTypeSystem().getTypeLoader( loaderClass );
  }
  
  public static <T extends ITypeLoader> T getTypeLoader( Class<? extends T> loaderClass, IModule module )
  {
    return CommonServices.getTypeSystem().getTypeLoader( loaderClass, module );
  }

  public static ITypeLoader getTypeLoader( IGosuClass loaderClass )
  {
    return CommonServices.getTypeSystem().getTypeLoader( loaderClass );
  }

  public static String getNameOfParams( IType[] paramTypes, boolean bRelative, boolean bWithEnclosingType )
  {
    return CommonServices.getTypeSystem().getNameOfParams( paramTypes, bRelative, bWithEnclosingType );
  }

  public static ISymbolTable getCompiledGosuClassSymbolTable()
  {
    return CommonServices.getTypeSystem().getCompiledGosuClassSymbolTable();
  }

  public static void clearNameCaches()
  {
    CommonServices.getTypeSystem().clearNameCaches();
  }

  public static List<ITypeLoader> getGlobalTypeLoadersForTesting()
  {
    return CommonServices.getTypeSystem().getGlobalTypeLoadersForTesting();
  }

  public static void removeAllTypeLoaders()
  {
    CommonServices.getTypeSystem().removeAllTypeLoaders();
  }

  public static String getGenericRelativeName( IType type, boolean bRelativeBounds )
  {
    return getGenericName( type, true, bRelativeBounds );
  }

  public static String getGenericName( IType type )
  {
    return getGenericName( type, false, false );
  }

  public static String getGenericName( IType type, boolean bRelative, boolean bRelativeBounds )
  {
    if( !type.isGenericType() || type.isParameterizedType() )
    {
      return bRelative ? type.getRelativeName() : type.getName();
    }

    StringBuilder sb = new StringBuilder( (bRelative ? type.getRelativeName() : type.getName()) + "<" );
    IGenericTypeVariable[] typeVars = type.getGenericTypeVariables();
    for( int i = 0; i < typeVars.length; i++ )
    {
      IGenericTypeVariable typeVar = typeVars[i];
      sb.append( typeVar.getNameWithBounds( bRelativeBounds ) );
      if( i < typeVars.length - 1 )
      {
        sb.append( ',' );
      }
    }
    sb.append( '>' );
    return sb.toString();
  }

  public static List<CharSequence> getFullyQualifiedClassNameFromRelativeName( String strRelativeName )
  {
    return CommonServices.getTypeSystem().getFullyQualifiedClassNameFromRelativeName( strRelativeName );
  }
  public static IPropertyInfo getPropertyInfo( IType classBean, String strProperty, IFeatureFilter filter, IParserPart parserBase, IScriptabilityModifier scriptabilityConstraint) throws ParseException
  {
    return CommonServices.getGosuIndustrialPark().getPropertyInfo( classBean, strProperty, filter, parserBase, scriptabilityConstraint );
  }
  public static List<? extends IPropertyInfo> getProperties( ITypeInfo beanInfo, IType classSource )
  {
    return CommonServices.getGosuIndustrialPark().getProperties( beanInfo, classSource );
  }
  public static List<? extends IMethodInfo> getMethods( ITypeInfo beanInfo, IType ownersIntrinsicType )
  {
    return CommonServices.getGosuIndustrialPark().getMethods( beanInfo, ownersIntrinsicType );
  }

  public static boolean isDescriptorHidden( IAttributedFeatureInfo pi )
  {
    return CommonServices.getGosuIndustrialPark().isDescriptorHidden( pi );
  }

  public static Set<CharSequence> getAllIdeLoadableTypeNames( IProgressCallback progress, boolean bInternalMode )
  {
    return CommonServices.getTypeSystem().getAllIdeLoadableTypeNames( progress, bInternalMode );
  }

  public static List<Throwable> getTypeLoaderErrors()
  {
    return CommonServices.getTypeSystem().getTypeLoaderErrors();
  }


  public static Collection<? extends String> getAllHandledPrefixes()
  {
    return CommonServices.getTypeSystem().getAllHandledPrefixes();
  }

  public static IType getJavaType(Class javaClass) {
    return CommonServices.getTypeSystem().getJavaType(javaClass);
  }

  public static String getNameWithQualifiedTypeVariables(IType type) {
    return CommonServices.getTypeSystem().getNameWithQualifiedTypeVariables(type);
  }

  public static IType getDefaultParameterizedType(IType type) {
    return CommonServices.getTypeSystem().getDefaultParameterizedType(type);
  }

  public static boolean canCast(IType lhsType, IType rhsType) {
    return CommonServices.getTypeSystem().canCast(lhsType, rhsType);
  }

  public static void removeTypeLoaderListener(ITypeLoaderListener listener) {
    CommonServices.getTypeSystem().removeTypeLoaderListener(listener);
  }

  public static IJavaType getPrimitiveType(String name) {
    return CommonServices.getTypeSystem().getPrimitiveType(name);
  }

  public static IType getPrimitiveType(IType boxType) {
    return CommonServices.getTypeSystem().getPrimitiveType(boxType);
  }

  public static IType getBoxType(IType primitiveType) {
    return CommonServices.getTypeSystem().getBoxType(primitiveType);
  }

  public static IType[] boxPrimitiveTypeParams( IType[] typeParams )
  {
    IType[] newTypes = new IType[typeParams.length];
    for( int i = 0; i < typeParams.length; i++ )
    {
      if( typeParams[i].isPrimitive() )
      {
        newTypes[i] = TypeSystem.getBoxType( typeParams[i] );
      }
      else
      {
        newTypes[i] = typeParams[i];
      }
    }
    return newTypes;
  }

  public static IExecutionEnvironment getExecutionEnvironment()
  {
    return CommonServices.getTypeSystem().getExecutionEnvironment();
  }

  public static IModule getCurrentModule()
  {
    return CommonServices.getTypeSystem().getCurrentModule();
  }

  public static void setResourceFileResolver(ResourceFileResolver resolver) {
    CommonServices.getTypeSystem().setResourceFileResolver(resolver);
  }

  /**
   * IMPORTANT: The only time you should call this method is:
   * 1) within a class implementing IType, or
   * 2) wrapping a call to a Type constructor, typically within a type loader
   *   e.g., TypeSystem.getOrCreateTypeReference( new MyVeryOwnType() )
   *
   * Gets or creates a type ref for the specified type.
   *
   * @param type A raw or proxied type.
   * @return If the type is already a reference, returns the type as-is, otherwise creates and returns a new type ref.
   */
  public static ITypeRef getOrCreateTypeReference( IType type )
  {
    return CommonServices.getTypeSystem().getOrCreateTypeReference( type );
  }

  public static Class<? extends ITypeRef> getOrCreateTypeClassProxy( String strTypeClass )
  {
    return CommonServices.getTypeSystem().getOrCreateTypeClassProxy( strTypeClass );
  }

  /**
   * IMPORTANT: The only time you should call this method is:
   * 1) wrapping a call to a Type constructor, typically within a type loader
   *   e.g., TypeSystem.getOrCreateTypeReference( new MyVeryOwnType() )
   *
   * Do NOT call this when creating the type.  Instead call getOrCreateTypeReference
   * Gets or creates a type ref for the specified type.
   *
   * This method will NOT update the type reference in the proxy.
   *
   * @param type A raw or proxied type.
   * @return returns the already created type reference or throws if the ref does not exist
   */
  public static ITypeRef getTypeReference( IType type )
  {
    return CommonServices.getTypeSystem().getTypeReference( type );
  }

  public static void refreshAndClearCaches() {
    CommonServices.getTypeSystem().refreshAndClearCaches();
  }

  public static IType getTypeFromObject( Object obj )
  {
    return CommonServices.getTypeSystem().getTypeFromObject( obj );
  }

  /**
   * Parses a type name such as Iterable&lt;Claim&gt;.
   * @param typeName the name to parse
   * @return the type
   */
  public static IType parseTypeLiteral(String typeName) {
    try {
      IType type = GosuParserFactory.createParser(typeName).parseTypeLiteral(null).getType().getType();
      if (type instanceof IErrorType) {
        throw new RuntimeException("Type not found: " + typeName);
      }
      return type;
    } catch (ParseResultsException e) {
      throw new RuntimeException("Type not found: " + typeName, e);
    }
  }

  public static boolean isExpandable( IType type )
  {
    return CommonServices.getTypeSystem().isExpandable( type );
  }

  public static IType getExpandableComponentType( IType type )
  {
    return CommonServices.getTypeSystem().getExpandableComponentType( type );
  }

  public static IType boundTypes(IType targetType, List<IType> typesToBound) {
    return CommonServices.getTypeSystem().boundTypes(targetType, typesToBound);
  }

  public static IJavaClassInfo getJavaClassInfo(Class jClass) {
    return CommonServices.getJavaClassInfoProvider().getJavaClassInfo(jClass);
  }

  public static IJavaClassInfo getDefaultJavaClassInfo(Class jClass) {
    return CommonServices.getTypeSystem().getDefaultJavaClassInfo(jClass);
  }

  public static ICompoundType getCompoundType(IType... componentTypes) {
    return CommonServices.getTypeSystem().getCompoundType(componentTypes);
  }

  public static ICompoundType getCompoundType(Iterable<? extends IType> componentTypes) {
    return CommonServices.getTypeSystem().getCompoundType(componentTypes);
  }

  public static Method[] getDeclaredMethods( Class cls )
  {
    return CommonServices.getGosuIndustrialPark().getDeclaredMethods( cls );
  }

  public static boolean isBytecodeType( IType type )
  {
    return type instanceof IJavaType ||
     type instanceof IGosuClass ||
     type instanceof IGosuArrayClass ||
     type instanceof IJavaArrayType;
  }

  public static IType getTypeFromClass( Class cls )
  {
    ClassLoader cl = cls.getClassLoader();
    IModule module = null;
    if( cl instanceof IGosuClassLoader )
    {
      ModuleClassLoader moduleClassLoader = (ModuleClassLoader)cl.getParent();
      module = moduleClassLoader.getModule();
      TypeSystem.getExecutionEnvironment().pushModule( module );
    }
    try
    {
      return TypeSystem.get( cls );
    }
    finally
    {
      if( module != null )
      {
        TypeSystem.getExecutionEnvironment().popModule( module );
      }
    }
  }
}


