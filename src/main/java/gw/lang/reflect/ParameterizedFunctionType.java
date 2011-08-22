package gw.lang.reflect;

import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.IScriptPartId;

import java.io.ObjectStreamException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ParameterizedFunctionType implements IFunctionType
{
  private FunctionType _genericFuncType;
  private IType[] _typeParams;

  transient private FunctionTypeInfo _typeInfo;
  transient private IType _retType;
  transient private Set<IType> _allTypesInHierarchy;
  transient private IType[] _paramTypes;
  transient private CaseInsensitiveCharSequence _signature;


  public ParameterizedFunctionType( FunctionType genericType, IType... typeParams )
  {
    _genericFuncType = genericType;
    _typeParams = typeParams;
  }

  public IType getReturnType()
  {
    if( _retType != null )
    {
      return _retType;
    }

    IGenericMethodInfo gmi = (IGenericMethodInfo)_genericFuncType.getMethodInfo();
    gmi = gmi == null ? _genericFuncType : gmi;
    return _retType = gmi.getParameterizedReturnType( _typeParams );
  }

  public IType[] getParameterTypes()
  {
    if( _paramTypes != null )
    {
      return _paramTypes;
    }

    IGenericMethodInfo gmi = (gw.lang.reflect.IGenericMethodInfo)_genericFuncType.getMethodInfo();
    gmi = gmi == null ? _genericFuncType : gmi;
    return _paramTypes = gmi.getParameterizedParameterTypes( _typeParams );
  }

  public IMethodInfo getMethodInfo()
  {
    return _genericFuncType.getMethodInfo();
  }

  public CaseInsensitiveCharSequence getParamSignature()
  {
    if( _signature == null )
    {
      String strParams = _genericFuncType.getName() + "(";
      for( int i = 0; i < _paramTypes.length; i++ )
      {
        strParams += (i == 0 ? "" : ", " ) + (_paramTypes[i] == null ? "" : _paramTypes[i].getName());
      }
      strParams += ")";

      _signature = CaseInsensitiveCharSequence.get( strParams );
    }

    return _signature;
  }

  public String getName()
  {
    return _genericFuncType.getName();
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
    return _genericFuncType.getNamespace();
  }

  public ITypeLoader getTypeLoader()
  {
    return _genericFuncType.getTypeLoader();
  }

  public boolean isInterface()
  {
    return _genericFuncType.isInterface();
  }

  public List<? extends IType> getInterfaces()
  {
    return _genericFuncType.getInterfaces();
  }

  public boolean isEnum()
  {
    return false;
  }

  public IType getSupertype()
  {
    return _genericFuncType.getSupertype();
  }

  public IType getEnclosingType()
  {
    return _genericFuncType.getEnclosingType();
  }

  public IType getGenericType()
  {
    return isGenericType() ? this : _genericFuncType;
  }

  public boolean isFinal()
  {
    return true;
  }

  public boolean isParameterizedType()
  {
    return true;
  }

  public boolean isGenericType()
  {
    return false;
  }

  public IGenericTypeVariable[] getGenericTypeVariables()
  {
    return null;
  }

  public IFunctionType inferParameterizedTypeFromArgTypesAndContextType(IType[] eArgs, IType ctxType)
  {
    throw new IllegalStateException( "Tried to infer parameterization on an already parameterized type." );
  }

  public ParameterizedFunctionType getParameterizedType( IType... paramTypes )
  {
    throw new IllegalStateException( "Tried to parameterize an already parameterized type." );
  }

  public IType[] getTypeParameters()
  {
    return _typeParams;
  }

  public Set<IType> getAllTypesInHierarchy()
  {
    if( _allTypesInHierarchy == null )
    {
      _allTypesInHierarchy = new HashSet<IType>( 2 );
      _allTypesInHierarchy.add( this );
      _allTypesInHierarchy.add( _genericFuncType );
    }
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
    return null;
  }

  public Object makeArrayInstance( int iLength )
  {
    throw new UnsupportedOperationException( "Can't create an array of a parameterized function type." );
  }

  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    return null;
  }

  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    throw new UnsupportedOperationException();
  }

  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    return 0;
  }

  public IType getComponentType()
  {
    return null;
  }

  public boolean isAssignableFrom( IType type )
  {
    return type.getAllTypesInHierarchy().contains( this );
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
    return _genericFuncType.getParameterizedType( _typeParams );
  }

  public boolean isValid()
  {
    return true;
  }

  public int getModifiers()
  {
    return _typeInfo != null ? Modifier.getModifiersFrom( _typeInfo ) : Modifier.PUBLIC;
  }

  public boolean isAbstract()
  {
    return false;
  }

  public IScriptPartId getContext()
  {
    return _genericFuncType.getScriptPart();
  }

  public void setContext( IScriptPartId partId )
  {
    _genericFuncType.setScriptPart( partId );
  }

  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( !(o instanceof ParameterizedFunctionType) )
    {
      return false;
    }

    final ParameterizedFunctionType functionType = (ParameterizedFunctionType)o;

    return getParamSignature().equals( functionType.getParamSignature() );
  }

  public int hashCode()
  {
    return getParamSignature().hashCode();
  }

  public String toString()
  {
    return getParamSignature().toString();
  }

  public boolean areParamsCompatible( IFunctionType rhsType )
  {
    IType[] lhsParams = getParameterTypes();
    IType[] rhsParams = rhsType.getParameterTypes();

    if( lhsParams.length != rhsParams.length )
    {
      return false;
    }

    for( int i = 0; i < rhsParams.length; i++ )
    {
      IType myParamType = lhsParams[i];
      IType otherParamType = rhsParams[i];
      if( !(otherParamType.isAssignableFrom( myParamType ) || myParamType instanceof ITypeVariableType) )
      {
        return false;
      }
    }
    return true;
  }

  public IScriptPartId getScriptPart()
  {
    return getContext();
  }

  @Override
  public IType newInstance( IType[] paramTypes, IType returnType )
  {
    return new ParameterizedFunctionType( (FunctionType)_genericFuncType.newInstance( paramTypes, returnType ), _typeParams );
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
    if( _genericFuncType != null )
    {
      return _genericFuncType.getParameterNames();
    }
    return new String[0];
  }

  @Override
  public Object[] getDefaultValues()
  {
    if( _genericFuncType != null )
    {
      return _genericFuncType.getDefaultValues();
    }
    return new Object[0];
  }

  @Override
  public boolean hasOptionalParams()
  {
    if( _genericFuncType != null )
    {
      return _genericFuncType.hasOptionalParams();
    }
    return false;
  }
}
