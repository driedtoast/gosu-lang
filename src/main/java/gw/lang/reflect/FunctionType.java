package gw.lang.reflect;

import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.IBlockClass;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ScriptPartId;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;
import gw.util.concurrent.LazyVar;

import java.io.ObjectStreamException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * An IFunctionType implementation suitable for exposing Function types.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class FunctionType implements IFunctionType, IGenericMethodInfo
{
  public static final ThreadLocal<IFunctionType> CURRENT = new ThreadLocal<IFunctionType>();
  private static final IGenericTypeVariable[] EMPTY_TYPE_VARS = new IGenericTypeVariable[0];
  private static final IType[] EMPTY_ARGS = new IType[0];

  private IType _retType;
  private volatile IType[] _paramTypes;
  private IMethodInfo _mi;
  private String _strFunctionName;
  private IScriptPartId _scriptPart;
  private volatile IGenericTypeVariable[] _typeVars;
  private int _iModifiers;
  transient private FunctionTypeInfo _typeInfo;
  transient protected Set<IType> _allTypesInHierarchy;
  transient private CaseInsensitiveCharSequence _signature;
  volatile transient private Map<String, ParameterizedFunctionType> _parameterizationByParamsName;
  private LazyVar<FunctionArrayType> _arrType =
    new LazyVar<FunctionArrayType>( TypeSystem.getGlobalLock() )
    {
      @Override
      protected FunctionArrayType init()
      {
        return new FunctionArrayType( FunctionType.this, getFunctionClass(), getTypeLoader() );
      }
    };

  /**
   * Construct a FunctionType with the specified return type and parameter types array.
   *
   * @param strFunctionName The name of the function
   * @param retType         The return type of the function.
   * @param paramTypes      The parameter types. Can be null if no params.
   */
  public FunctionType( String strFunctionName, IType retType, IType[] paramTypes )
  {
    _retType = retType;
    if (_retType == null) {
      _retType = IJavaType.pVOID;
    }
    _paramTypes = paramTypes == null || paramTypes.length == 0 ? EMPTY_ARGS : paramTypes;
    _strFunctionName = strFunctionName;
    _mi = null;
    _allTypesInHierarchy = Collections.<IType>singleton( this );
    _typeVars = EMPTY_TYPE_VARS;
  }

  /**
   * Construct a generic FunctionType with the specified return type, parameter
   * types, and generic type variables.
   *
   * @param strFunctionName The name of the function
   * @param retType         The return type of the function.
   * @param paramTypes      The parameter types. Can be null if no params.
   * @param typeVars        The generic type variables. If null, does not create a
   *                        generic function type.
   */
  public FunctionType( String strFunctionName, IType retType, IType[] paramTypes, IGenericTypeVariable[] typeVars )
  {
    this( strFunctionName, retType, paramTypes );
    if( typeVars != null )
    {
      _typeVars = typeVars;
      for( IGenericTypeVariable gtv : typeVars )
      {
        gtv.getTypeVariableDefinition().setEnclosingType( this );
      }
    }
  }

  public FunctionType( IMethodInfo mi )
  {
    this(mi, false);
  }

  public FunctionType( IMethodInfo mi, boolean lazyTypes )
  {
    _mi = mi;

    if (!lazyTypes) {
      initLazyMethodInfoState();
    }
    _strFunctionName = mi.getDisplayName();
    _allTypesInHierarchy = Collections.<IType>singleton( this );
  }

  private void initLazyMethodInfoState() {
    TypeSystem.lock();
    try {
      if (_paramTypes == null) {
        IParameterInfo[] pd = _mi.getParameters();
        int iArgs = pd.length;
        _paramTypes = new IType[iArgs];
        for( int i = 0; i < iArgs; i++ )
        {
          _paramTypes[i] = pd[i].getFeatureType();
        }
        if (_paramTypes.length == 0) {
          _paramTypes = EMPTY_ARGS;
        }
        _typeVars = EMPTY_TYPE_VARS;
        if( _mi instanceof IGenericMethodInfo)
        {
          _typeVars = ((IGenericMethodInfo)_mi).getTypeVariables();
        }
        clearParamSignature();
      }
      _retType = _mi.getReturnType();
      if (_retType == null) {
        _retType = IJavaType.pVOID;
      }
    } finally {
      TypeSystem.unlock();
    }
  }

  public FunctionType( FunctionType source, IGosuClass gsClass )
  {
    if( gsClass.isParameterizedType() )
    {
      TypeVarToTypeMap actualParamByVarName = TypeSystem.mapTypeByVarName( gsClass, gsClass, true );

      IGenericTypeVariable[] tvs = source.getTypeVariables();
      if( tvs != null )
      {
        for( IGenericTypeVariable tv : tvs )
        {
          if( actualParamByVarName.isEmpty() )
          {
            actualParamByVarName = new TypeVarToTypeMap();
          }
          actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), tv.getTypeVariableDefinition().getType() );
        }
      }
      assignReturnTypeFromTypeParams( source, actualParamByVarName, true );
      assignParamTypesFromTypeParams( source, actualParamByVarName, true );
    }
    else
    {
      _retType = source.getReturnType();
      if (_retType == null) {
        _retType = IJavaType.pVOID;
      }
      _paramTypes = source._paramTypes;
      //clearParamSignature(); TODO - reenable if you dare
    }
    copyFields( source );
  }

  protected FunctionType(FunctionType source, IType returnType, IType[] paramTypes) {
    _retType = returnType;
    _paramTypes = paramTypes;
    copyFields( source );
  }

  protected FunctionType( FunctionType source, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars )
  {
    assignReturnTypeFromTypeParams( source, actualParamByVarName, bKeepTypeVars );
    assignParamTypesFromTypeParams( source, actualParamByVarName, bKeepTypeVars );
    copyFields( source );
    clearParamSignature();
  }

  protected void copyFields( FunctionType source )
  {
    _mi = source._mi;
    _strFunctionName = source._strFunctionName;
    _scriptPart = source._scriptPart;
    _typeVars = source.getGenericTypeVariables();
    _typeInfo = source._typeInfo;
    _allTypesInHierarchy = source._allTypesInHierarchy;
    _signature = source._signature;
    _parameterizationByParamsName = source._parameterizationByParamsName;
  }

  /**
   * Create a new FunctionType based on the type parameters assigned to the type
   * vars in actualParamByVarName.
   * <p>
   * It is important to note that this form of parameterization is different
   * from calling getParameterizedType(). The latter creates a parameterization
   * of a generic function e.g., function foo&lt;T&gt;() where T is a type var
   * on the function.
   * <p>
   * This here method is for parameterizing a function type that has references
   * to external type vars e.g., class Fred&lt;T&gt; { function foo( t : T ){} }
   * where T is a type var on the containing class.
   *
   * @param source The raw form of a FunctionType having possible references to
   *   type vars defined in its containing class.
   * @param actualParamByVarName A map of concrete types by type var name
   *   e.g., "T" -> String
   * @param bKeepTypeVars Indicates whether or not a type var referenced in the
   *   source FunctionType that cannot be resolved via the actualParamByVarName
   *   map should remain as a TypeVariableType or should be converted to its
   *   bounding type.
   * @return A parameterized version of the source FunctionType
   */
  public FunctionType parameterize( FunctionType source, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars )
  {
    return new FunctionType( source, actualParamByVarName, bKeepTypeVars );
  }

  private void assignParamTypesFromTypeParams( FunctionType source, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars )
  {
    IType[] genParamTypes = source.getParameterTypes();
    if( genParamTypes != null )
    {
      _paramTypes = new IType[genParamTypes.length];
      for( int j = 0; j < genParamTypes.length; j++ )
      {
        _paramTypes[j] = TypeSystem.getActualType( genParamTypes[j], actualParamByVarName, bKeepTypeVars );
        if( _paramTypes[j] == null )
        {
          _paramTypes[j] = genParamTypes[j];
        }
      }
      if (_paramTypes.length == 0) {
        _paramTypes = EMPTY_ARGS;
      }
      clearParamSignature();
    }
  }

  private void assignReturnTypeFromTypeParams( FunctionType source, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars )
  {
    _retType = TypeSystem.getActualType( source.getReturnType(), actualParamByVarName, bKeepTypeVars );
    if( _retType == null )
    {
      _retType = source.getReturnType();
    }
    if (_retType == null) {
      _retType = IJavaType.pVOID;
    }
  }

  /**
   * @return The instrinic type of this FunctionType's return type.
   */
  public IType getIntrinsicType()
  {
    return getReturnType();
  }

  public IType getReturnType()
  {
    if (_retType == null) {
      initLazyMethodInfoState();
    }
    return _retType;
  }

  public void setRetType( IType retType )
  {
    _retType = retType == null ? IJavaType.pVOID : retType;
  }

  public IType[] getParameterTypes()
  {
    if (_paramTypes == null) {
      initLazyMethodInfoState();
    }
    return _paramTypes;
  }

  public void setArgumentTypes( IType[] paramTypes )
  {
    _paramTypes = paramTypes == null || paramTypes.length == 0 ? EMPTY_ARGS : paramTypes;
    clearParamSignature();
  }

  public IMethodInfo getMethodInfo()
  {
    if( _mi == null )
    {
      if( getScriptPart() != null )
      {
        IType type = getScriptPart().getContainingType();
        if( type instanceof IGosuClass )
        {
          IGosuClass gsClass = (IGosuClass)type;
          _mi = gsClass.getTypeInfo().getMethod( type, getName(), getParameterTypes() );
        }
      }
    }
    return _mi;
  }

  public IFeatureInfo getMethodOrConstructorInfo()
  {
    IMethodInfo mi = getMethodInfo();
    if( mi == null )
    {
      if( getScriptPart() != null )
      {
        IType type = getScriptPart().getContainingType();
        if( type instanceof IGosuClass && type.getRelativeName().equals( getName() ) )
        {
          IGosuClass gsClass = (IGosuClass)type;
          return gsClass.getTypeInfo().getConstructor( type, getParameterTypes() );
        }
      }
    }
    return mi;
  }

  private void clearParamSignature()
  {
    _signature = null;
  }

  public CaseInsensitiveCharSequence getParamSignature()
  {
    if( _signature == null )
    {
      IType[] paramTypes = getParameterTypes();
      if( paramTypes.length == 0 )
      {
        return _signature = CaseInsensitiveCharSequence.get( _strFunctionName + "()" );
      }

      String strParams = _strFunctionName + "(";
      for( int i = 0; i < paramTypes.length; i++ )
      {
        strParams += (i == 0 ? "" : ", " ) + (paramTypes[i] == null ? "" : paramTypes[i].getName());
      }
      strParams += ")";

      _signature = CaseInsensitiveCharSequence.get( strParams );
    }

    return _signature;
  }

  public String getName()
  {
    return _strFunctionName;
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getRelativeName()
  {
    return getName();
  }

  public String getNamespace()
  {
    IType enclosingType = getEnclosingType();
    return enclosingType != null
           ? enclosingType.getName()
           : null;
  }

  public ITypeLoader getTypeLoader()
  {
    return null;
  }

  public boolean isInterface()
  {
    return false;
  }

  public List<? extends IType> getInterfaces()
  {
    return EMPTY_TYPE_LIST;
  }

  public boolean isEnum()
  {
    return false;
  }

  public IType getSupertype()
  {
    return null;
  }

  public IType getEnclosingType()
  {
    if( _scriptPart instanceof IType)
    {
      return (IType)_scriptPart;
    }
    if( getMethodInfo() != null )
    {
      return getMethodInfo().getOwnersType();
    }
    return null;
  }

  public IType getGenericType()
  {
    return isGenericType() ? this : null;
  }

  public boolean isFinal()
  {
    return false;
  }

  public boolean isParameterizedType()
  {
    return false;
  }

  public boolean isGenericType()
  {
    return getGenericTypeVariables().length > 0;
  }

  public IGenericTypeVariable[] getGenericTypeVariables()
  {
    if (_typeVars == null) {
      initLazyMethodInfoState();
    }
    return _typeVars;
  }

  public ParameterizedFunctionType getParameterizedType( IType... paramTypes )
  {
    if( paramTypes == null || paramTypes.length == 0 )
    {
      throw new IllegalArgumentException( "Parameter types required." );
    }

    if( _parameterizationByParamsName == null )
    {
      TypeSystem.lock();
      try
      {
        if( _parameterizationByParamsName == null )
        {
          _parameterizationByParamsName = new ConcurrentHashMap<String, ParameterizedFunctionType>( 2 );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }

    String strNameOfParams = TypeSystem.getNameOfParams( paramTypes, false, true );
    ParameterizedFunctionType parameterizedType = _parameterizationByParamsName.get( strNameOfParams );
    if( parameterizedType == null )
    {
      TypeSystem.lock();
      try
      {
        parameterizedType = _parameterizationByParamsName.get( strNameOfParams );
        if( parameterizedType == null )
        {
          parameterizedType = new ParameterizedFunctionType( this, paramTypes );
          _parameterizationByParamsName.put( strNameOfParams, parameterizedType );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return parameterizedType;
  }

  public IFunctionType inferParameterizedTypeFromArgTypesAndContextType(IType[] argTypes, IType ctxType)
  {
    TypeVarToTypeMap map = TypeVarToTypeMap.EMPTY_MAP;
    if( argTypes.length > 0 )
    {
      if( getMethodInfo() == null )
      {
        map = inferTypeParametersFromArgumentTypes( argTypes );
      }
      else
      {
        map = ((IGenericMethodInfo)getMethodInfo()).inferTypeParametersFromArgumentTypes( argTypes );
      }
    }

    IGenericTypeVariable[] typeVars = getGenericTypeVariables();
    if( typeVars.length == 0 )
    {
      return this;
    }

    IType[] typeParams = new IType[typeVars.length];
    for( int i = 0; i < typeVars.length; i++ )
    {
      IType inferredType = map.get( typeVars[i].getTypeVariableDefinition().getType() );
      if( inferredType == null && ctxType != null )
      {
        //try to infer type from context type
        TypeVarToTypeMap returnTypeVars = new TypeVarToTypeMap();
        TypeSystem.inferTypeVariableTypesFromGenParamTypeAndConcreteType( getReturnType(), ctxType, returnTypeVars );
        inferredType = returnTypeVars.get( typeVars[i].getTypeVariableDefinition().getType() );
      }
      typeParams[i] = inferredType;
      if( typeParams[i] == null )
      {
        return this;
      }
    }
    return getParameterizedType( typeParams );
  }

  public IType[] getTypeParameters()
  {
    return null;
  }

  public Set<IType> getAllTypesInHierarchy()
  {
    return _allTypesInHierarchy;
  }

  public boolean isArray()
  {
    return false;
  }

  public boolean isPrimitive()
  {
    return false;
  }

  public IType getArrayType()
  {
    return _arrType.get();
  }

  public Object makeArrayInstance( int iLength )
  {
    return TypeSystem.get( getFunctionClass() ).makeArrayInstance( iLength );
  }

  private Class<?> getFunctionClass()
  {
    return TypeSystem.getCurrentModule().getClassLoader().getGosuClassLoader().getFunctionClassForArity( getParameterTypes().length );
  }

  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    return TypeSystem.get( getFunctionClass() ).getArrayComponent( array, iIndex );
  }

  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    TypeSystem.get( getFunctionClass() ).setArrayComponent( array, iIndex, value );
  }

  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    return TypeSystem.get( getFunctionClass() ).getArrayLength( array );
  }

  public IType getComponentType()
  {
    return null;
  }

  public boolean isAssignableFrom( IType type )
  {
    if( this == type )
    {
      return true;
    }

    if( type instanceof IBlockClass )
    {
      return isAssignableFrom( ((IBlockClass)type).getBlockType() );
    }
    if( type instanceof FunctionType )
    {
      FunctionType otherType = (FunctionType)type;
      //contravariant arg types
      if( areParamsCompatible( this, otherType ) )
      {
        //covariant return types
        return getReturnType().isAssignableFrom( otherType.getReturnType() ) ||
               getReturnType() == IGosuParser.NULL_TYPE;
      }
    }
    return false;
  }

  public boolean areParamsCompatible(IFunctionType rhsFunctionType) {
    return areParamsCompatible(this, rhsFunctionType);
  }

  public static boolean areParamsCompatible( IFunctionType lhsType, IFunctionType rhsType )
  {
    IType[] lhsParams = lhsType.getParameterTypes();
    IType[] rhsParams = rhsType.getParameterTypes();

    if( lhsParams.length != rhsParams.length )
    {
      return false;
    }

    for( int i = 0; i < rhsParams.length; i++ )
    {
      IType myParamType = lhsParams[i];
      IType otherParamType = rhsParams[i];                    //## todo: this is a hack; we need to tighten this up
      if( !(otherParamType.isAssignableFrom( myParamType ) || myParamType instanceof ITypeVariableType) )
      {
        return false;
      }
    }
    return true;
  }

  public boolean isMutable()
  {
    return false;
  }

  public ITypeInfo getTypeInfo()
  {
    return _typeInfo == null ? _typeInfo = new FunctionTypeInfo( this ) : _typeInfo;
  }

  public void unloadTypeInfo()
  {
    _typeInfo = null;
  }

  public Object readResolve() throws ObjectStreamException
  {
    return this;
  }

  public boolean isValid()
  {
    return true;
  }

  public int getModifiers()
  {
    if( _iModifiers == 0 )
    {
      return _mi != null ? Modifier.getModifiersFrom( _mi ) : Modifier.PUBLIC;
    }
    return _iModifiers;
  }
  public void setModifiers( int iModifiers )
  {
    _iModifiers = iModifiers;
  }

  public boolean isAbstract()
  {
    return false;
  }

  public IScriptPartId getScriptPart()
  {
    return _scriptPart;
  }

  @Override
  public IType newInstance( IType[] paramTypes, IType returnType )
  {
    FunctionType functionType = new FunctionType(this._strFunctionName, returnType, paramTypes, cloneTypeVars() );
    if (getScriptPart() == null && _mi != null) {
      if (_mi instanceof MethodInfoDelegate) {
        functionType.setScriptPart(new ScriptPartId(((MethodInfoDelegate)_mi).getSource().getOwnersType(), null));
      } else {
        functionType.setScriptPart(new ScriptPartId(_mi.getOwnersType(), null));
      }
    } else {
      functionType.setScriptPart(getScriptPart());
    }
    return functionType;
  }

  private IGenericTypeVariable[] cloneTypeVars() {
    IGenericTypeVariable[] typeVars = new IGenericTypeVariable[_typeVars.length];
    for (int i = 0; i < typeVars.length; i++) {
      typeVars[i] = _typeVars[i].clone();
    }
    return typeVars;
  }

  public void setScriptPart( IScriptPartId scriptPart )
  {
    _scriptPart = scriptPart;
  }

  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( !getClass().isInstance( o ) )
    {
      return false;
    }

    final FunctionType funcType = (FunctionType)o;

    if( !funcType.getDisplayName().equals( getDisplayName() ) )
    {
      return false;
    }

    if( funcType.getParameterTypes().length != getParameterTypes().length )
    {
      return false;
    }

    for( int i = 0; i < getParameterTypes().length; i++ )
    {
      IType paramType = getParameterTypes()[i];
      IType otherParamType = funcType.getParameterTypes()[i];
      if( paramType instanceof INonLoadableType )
      {
        if(paramType instanceof ITypeVariableType && otherParamType instanceof ITypeVariableType) {
          if(!paramType.equals(otherParamType) &&
              !(((ITypeVariableType)paramType).getEnclosingType() == this
                  && ((ITypeVariableType)otherParamType).getEnclosingType() == funcType
                  && paramType.getRelativeName().equals(otherParamType.getRelativeName()))) {
            return false;
          }
        } else {
          if( paramType != otherParamType )
        {
          return false;
        }
      }
      }
      else if( !paramType.getName().equals( otherParamType.getName() ) )
      {
        return false;
      }
    }

    if(getReturnType() instanceof ITypeVariableType && funcType.getReturnType() instanceof ITypeVariableType) {
      return getReturnType().equals(funcType.getReturnType()) ||
        (getReturnType().getEnclosingType() == this && funcType.getReturnType().getEnclosingType() == funcType && getReturnType().getRelativeName().equals(funcType.getReturnType().getRelativeName()));
    } else {
    return getReturnType() instanceof INonLoadableType
           ? getReturnType() == funcType.getReturnType()
           : getReturnType().equals( funcType.getReturnType() );
  }
  }

  public int hashCode()
  {
    int result = getDisplayName().hashCode();
    for( int i = 0; i < getParameterTypes().length; i++ )
    {
      if( getParameterTypes()[i] instanceof INonLoadableType )
      {
        result = 31 * result + getParameterTypes()[i].hashCode();
      }
      else
      {
        result = 31 * result + getParameterTypes()[i].getName().hashCode();
      }
    }

    if( getReturnType() instanceof INonLoadableType )
    {
      result = 31 * result + getReturnType().hashCode();
    }
    else
    {
      result = 31 * result + getReturnType().getName().hashCode();
    }
    return result;
  }

  public String toString()
  {
    return getParamSignature().toString() + ":" + getReturnType().getName();
  }

  public TypeVarToTypeMap inferTypeParametersFromArgumentTypes( IType... argTypes )
  {
    IType[] genParamTypes = getParameterTypes();
    TypeVarToTypeMap map = new TypeVarToTypeMap();
    for( int i = 0; i < argTypes.length; i++ )
    {
      if( genParamTypes.length > i )
      {
        TypeSystem.inferTypeVariableTypesFromGenParamTypeAndConcreteType( genParamTypes[i], argTypes[i], map );
      }
    }
    return map;
  }

  public IGenericTypeVariable[] getTypeVariables()
  {
    return getGenericTypeVariables();
  }

  public IType getParameterizedReturnType( IType... typeParams )
  {
    TypeVarToTypeMap actualParamByVarName = new TypeVarToTypeMap();
    int i = 0;
    for( IGenericTypeVariable tv : getTypeVariables() )
    {
      actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), typeParams[i++] );
    }
    return TypeSystem.getActualType( getReturnType(), actualParamByVarName, false );
  }

  public IType[] getParameterizedParameterTypes( IType... typeParams )
  {
    TypeVarToTypeMap actualParamByVarName = new TypeVarToTypeMap();
    int i = 0;
    for( IGenericTypeVariable tv : getTypeVariables() )
    {
      actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), typeParams[i++] );
    }

    IType[] genParamTypes = getParameterTypes();
    IType[] paramTypes = new IType[genParamTypes.length];
    for( int j = 0; j < genParamTypes.length; j++ )
    {
      paramTypes[j] = TypeSystem.getActualType( genParamTypes[j], actualParamByVarName, false );
    }
    return paramTypes;
  }

  public IType getRuntimeType()
  {
    TypeVarToTypeMap actualParamByVarName = new TypeVarToTypeMap();

    actualParamByVarName = mapTypes( actualParamByVarName, getParameterTypes() );
    actualParamByVarName = mapTypes( actualParamByVarName, getReturnType() );

    return actualParamByVarName.size() != 0
           ? parameterize( this, actualParamByVarName, false )
           : this;
  }

  //Move Intrinsic type helper up here 
  private TypeVarToTypeMap mapTypes( TypeVarToTypeMap actualParamByVarName, IType... types )
  {
    for( int i = 0; i < types.length; i++ )
    {
      IType type = types[i];
      if( type instanceof ITypeVariableType )
      {
        actualParamByVarName.put( (ITypeVariableType)types[i], types[i] );
      }
      if( type instanceof ITypeVariableArrayType )
      {
        mapTypes( actualParamByVarName, type.getComponentType() );
      }
      if( type.isParameterizedType() )
      {
        IType[] parameters = type.getTypeParameters();
        mapTypes( actualParamByVarName, parameters );
      }
      if( type instanceof IFunctionType )
      {
        IFunctionType funType = (IFunctionType)type;
        mapTypes( actualParamByVarName, funType.getReturnType() );
        IType[] paramTypes = funType.getParameterTypes();
        for( IType paramType : paramTypes )
        {
          mapTypes( actualParamByVarName, paramType );
        }
      }
    }
    return actualParamByVarName;
  }

  public boolean hasTypeVariable( ITypeVariableType tvt )
  {
    if( !isGenericType() )
    {
      return false;
    }

    for( IGenericTypeVariable gtv : getGenericTypeVariables() )
    {
      if( tvt.getName().equals( gtv.getName() ) )
      {
        return true;
      }
    }

    return false;
  }

  public boolean isDiscarded()
  {
    return false;
  }

  public void setDiscarded( boolean bDiscarded )
  {
  }

  public boolean isCompoundType()
  {
    return false;
  }

  public Set<IType> getCompoundTypeComponents()
  {
    return null;
  }

  @Override
  public String[] getParameterNames()
  {
    IFeatureInfo miOrCi = getMethodOrConstructorInfo();
    if( miOrCi instanceof IOptionalParamCapable )
    {
      return ((IOptionalParamCapable)miOrCi).getParameterNames();
    }
    return new String[0];
  }

  private static final Object[] EMPPTY_OBJECT_ARRAY = new Object[0];
  @Override
  public Object[] getDefaultValues()
  {
    if( getMethodInfo() instanceof IOptionalParamCapable )
    {
      return ((IOptionalParamCapable)getMethodInfo()).getDefaultValues();
    }
    return EMPPTY_OBJECT_ARRAY;
  }

  @Override
  public boolean hasOptionalParams()
  {
    for( Object o : getDefaultValues() )
    {
      if( o != null )
      {
        return true;
      }
    }
    return false;
  }
}
