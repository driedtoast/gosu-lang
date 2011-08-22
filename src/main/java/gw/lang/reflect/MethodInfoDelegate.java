package gw.lang.reflect;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.gs.IGenericTypeVariable;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class MethodInfoDelegate implements IMethodInfo, IGenericMethodInfo, IMethodInfoDelegate, IOptionalParamCapable
{
  private IMethodInfo _mi;
  private IFeatureInfo _container;

  /**
   * @param container Typically this will be the containing ITypeInfo
   * @param source    The method info source
   */
  public MethodInfoDelegate( IFeatureInfo container, IMethodInfo source )
  {
    _container = container;
    _mi = source;
  }

  public IParameterInfo[] getParameters()
  {
    return _mi.getParameters();
  }

  public IType getReturnType()
  {
    return _mi.getReturnType();
  }

  public IMethodCallHandler getCallHandler()
  {
    return _mi.getCallHandler();
  }

  public String getReturnDescription()
  {
    return _mi.getReturnDescription();
  }

  public List<IExceptionInfo> getExceptions()
  {
    return _mi.getExceptions();
  }

  public IFeatureInfo getContainer()
  {
    return _container;
  }

  public IType getOwnersType()
  {
    return _container.getOwnersType();
  }

  public String getName()
  {
    return _mi.getName();
  }

  public String getDisplayName()
  {
    return _mi.getDisplayName();
  }

  public String getDescription()
  {
    return _mi.getDescription();
  }

  public boolean isScriptable()
  {
    return _mi.isScriptable();
  }

  public boolean isDeprecated()
  {
    return _mi.isDeprecated();
  }

  public String getDeprecatedReason()
  {
    return _mi.getDeprecatedReason();
  }

  public boolean isVisible( IScriptabilityModifier constraint )
  {
    return _mi.isVisible( constraint );
  }

  public boolean isHidden()
  {
    return _mi.isHidden();
  }

  public boolean isStatic()
  {
    return _mi.isStatic();
  }

  public boolean isPrivate()
  {
    return _mi.isPrivate();
  }

  public boolean isInternal()
  {
    return _mi.isInternal();
  }

  public boolean isProtected()
  {
    return _mi.isProtected();
  }

  public boolean isPublic()
  {
    return _mi.isPublic();
  }

  public boolean isAbstract()
  {
    return _mi.isAbstract();
  }

  public boolean isFinal()
  {
    return _mi.isFinal();
  }

  public List<IAnnotationInfo> getAnnotations()
  {
    return _mi.getAnnotations();
  }

  public List<IAnnotationInfo> getAnnotationsOfType( IType type )
  {
    return _mi.getAnnotationsOfType( type );
  }

  public boolean hasAnnotation( IType type )
  {
    return _mi.hasAnnotation( type );
  }

  public IMethodInfo getSource()
  {
    return _mi;
  }

  public IGenericTypeVariable[] getTypeVariables()
  {
    if( _mi instanceof IGenericMethodInfo )
    {
      return ((IGenericMethodInfo)_mi).getTypeVariables();
    }
    return new IGenericTypeVariable[0];
  }

  public IType getParameterizedReturnType( IType... typeParams )
  {
    if( _mi instanceof IGenericMethodInfo )
    {
      return ((IGenericMethodInfo)_mi).getParameterizedReturnType( typeParams );
    }
    return null;
  }

  public IType[] getParameterizedParameterTypes( IType... typeParams )
  {
    if( _mi instanceof IGenericMethodInfo )
    {
      return ((IGenericMethodInfo)_mi).getParameterizedParameterTypes( typeParams );
    }
    return IType.EMPTY_ARRAY;
  }

  public TypeVarToTypeMap inferTypeParametersFromArgumentTypes( IType... argTypes )
  {
    if( _mi instanceof IGenericMethodInfo )
    {
      return ((IGenericMethodInfo)_mi).inferTypeParametersFromArgumentTypes( argTypes );
    }
    return TypeVarToTypeMap.EMPTY_MAP;
  }

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return _mi.getDeclaredAnnotations();
  }

  @Override
  public IAnnotationInfo getAnnotation( IType type )
  {
    return _mi.getAnnotation( type );
  }

  @Override
  public boolean hasDeclaredAnnotation( IType type )
  {
    return _mi.hasDeclaredAnnotation( type );
  }

  @Override
  public String toString()
  {
    return _mi.toString();
  }

  @Override
  public Object[] getDefaultValues()
  {
    if( _mi instanceof IOptionalParamCapable )
    {
      return ((IOptionalParamCapable)_mi).getDefaultValues();
    }
    return new Object[0];
  }

  @Override
  public String[] getParameterNames()
  {
    if( _mi instanceof IOptionalParamCapable )
    {
      return ((IOptionalParamCapable)_mi).getParameterNames();
    }
    return new String[0];
  }
}
