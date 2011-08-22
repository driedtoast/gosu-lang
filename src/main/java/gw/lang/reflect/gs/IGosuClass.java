package gw.lang.reflect.gs;

import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.ICompilationState;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.reflect.IEnumType;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IHasInnerClass;
import gw.lang.parser.IManagedContext;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.FunctionType;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IModifierInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IEnhanceableType;
import gw.lang.reflect.IHasJavaClass;
import gw.lang.reflect.java.IJavaType;

import java.util.List;
import java.util.Map;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuClass extends IType, ICompilableType, IEnumType, IEnhanceableType, IGosuResource, ISourceRoot, Comparable, IHasInnerClass, IHasJavaClass
{
  String PROXY_PREFIX = "_proxy_";
  String SUPER_PROXY_CLASS_PREFIX = "_java_";
  String ANONYMOUS_PREFIX = "AnonymouS_";
  IFunctionType DEF_CTOR_TYPE = new FunctionType( "__def_ctor", IJavaType.pVOID, null );


  /**
   * @return The parsed element corresponding with this source root.
   */
  public IParsedElement getParsedElement();

  IGosuClassTypeInfo getTypeInfo();

  boolean isStatic();

  IModifierInfo getModifierInfo();

  boolean isSubClass( IType gsSubType );

  boolean isCompiled();

  Map<CharSequence, ? extends IGosuClass> getInnerClasses();

  IType resolveRelativeInnerClass( String strRelativeInnerClassName, boolean bForce );

  IClassStatement getClassStatement();

  IClassStatement getClassStatementWithoutCompile();
  
  ICompilationState getCompilationState();

  boolean isCompiling();

  boolean isCompilingHeader();

  boolean isHeaderCompiled();

  boolean isCompilingDeclarations();

  boolean isDeclarationsCompiled();

  boolean isInnerDeclarationsCompiled();

  boolean isCompilingDefinitions();

  boolean isDefinitionsCompiled();

  ISourceFileHandle getSourceFileHandle();

  boolean isTestClass();

  boolean hasError();

  boolean hasWarnings();

  IManagedContext getManagedContext();

  ITypeUsesMap getTypeUsesMap();

  ParseResultsException getParseResultsException();

  List<? extends IVarStatement> getMemberFields();

  List<? extends IDynamicFunctionSymbol> getMemberFunctions();

  Map<CaseInsensitiveCharSequence, ? extends IVarStatement> getMemberFieldsMap();
  
  public IDynamicPropertySymbol getMemberProperty( CaseInsensitiveCharSequence name );

  IType getEnclosingTypeReference();

  void setManagedContext( IManagedContext ctx );

  
  IJavaType getJavaType();

  /**
   * WARNING:  This method is slow the first time it is called.  It will iterate over all types in the system
   *  and find all matching subtypes
   * @return all subtypes of this type
   */
  List<? extends IType> getSubtypes();

  /**
   * Only for use during type loading e.g., from GosuClassTypeLoader
   * @param enclosingType the enclosing type
   */
  void setEnclosingType( IType enclosingType );
  void setNamespace( String strNamespace );

  boolean shouldKeepDebugInfo();

  boolean isAnonymous();

  IGosuParser getEditorParser();

  void setCreateEditorParser(boolean bEditorParser);

  Class<?> getBackingClass();
  void unloadBackingClass();
  boolean hasBackingClass();

  ISymbol getExternalSymbol( String strName );

  public List<? extends IJavaType> getJavaInterfaces();
  public IType findProxiedClassInHierarchy();

  Map<CharSequence, ? extends IGosuClass> getKnownInnerClassesWithoutCompiling();

  boolean isInstrumented();

  public List<IGosuClass> getBlocks();
  
  public IGosuClass getBlock( int i );

  void validateAncestry();

  void validateAncestry(List<IType> visited);

  static class ProxyUtil
  {
    public static boolean isProxy( IType type )
    {
      return type != null && isProxyClass( type.getName() );
    }
    public static boolean isProxyClass( String strName )
    {
      return strName != null &&
             strName.length() > PROXY_PREFIX.length() && // must be Greater than
             strName.startsWith( PROXY_PREFIX );
    }
    public static boolean isProxyStart( String strName )
    {
      return strName != null &&
             strName.length() >= PROXY_PREFIX.length() &&
             strName.startsWith( PROXY_PREFIX );
    }

    public static IType getProxiedType( IType type )
    {
      while (type.isParameterizedType()) {
        type = type.getGenericType();
      }
      return TypeSystem.getByFullName( getNameSansProxy( type ) );
    }
    
    public static String getNameSansProxy( IType type )
    {
      return getNameSansProxy( type.getName() );
    }
    public static String getNameSansProxy( String name )
    {
      if( name.startsWith( IGosuClass.PROXY_PREFIX ) && name.length() > IGosuClass.PROXY_PREFIX.length() + 1 )
      {
        return name.substring( IGosuClass.PROXY_PREFIX.length() + 1 );
      }

      return name;
    }
  }

}
