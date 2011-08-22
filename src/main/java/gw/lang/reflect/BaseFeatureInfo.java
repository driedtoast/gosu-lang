package gw.lang.reflect;

import gw.lang.Scriptable;
import gw.lang.annotation.ScriptabilityModifier;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.util.GosuCollectionUtil;
import gw.config.CommonServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.IdentityHashMap;


/**
 * Handles basic attribute setting/getting. Also handles visibility modifiers.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class BaseFeatureInfo implements IAttributedFeatureInfo
{
  private transient IType _intrType;
  private transient IFeatureInfo _container;
  private volatile List<IAnnotationInfo> _deprecated;

  transient private volatile List<IAnnotationInfo> _annotations;

  transient private volatile List<ScriptabilityModifier> _scriptablity;

  static <T> List<T> compactAndLockList(List<T> list) {
    if (list == null || list.isEmpty()) {
      return Collections.emptyList();
    }

    if (list.size() == 1) {
      return Collections.singletonList(list.get(0));
    }

    if (list instanceof ArrayList) {
      ((ArrayList<T>)list).trimToSize();
    }

    return Collections.unmodifiableList(list);
  }

  protected Collection<BaseFeatureInfo> getSuperAnnotatedElements()
  {
    List<BaseFeatureInfo> infos = new ArrayList<BaseFeatureInfo>();
    addAnnotationSuperElement( infos, getOwnersType().getSupertype() );
    if( !(this instanceof IConstructorInfo) )
    {
      List<? extends IType> interfaces = getOwnersType().getInterfaces();
      if( interfaces != null )
      {
        for( IType anInterface : interfaces )
        {
          addAnnotationSuperElement( infos, anInterface );
        }
      }
    }
    return infos;
  }

  private void addAnnotationSuperElement( List<BaseFeatureInfo> infos, IType type )
  {
    if( type != null && !(type instanceof IErrorType) )
    {
      IFeatureInfo featureInfo;
      if( this instanceof IConstructorInfo )
      {
        featureInfo = type.getTypeInfo().getConstructor( getParamTypes( ((IConstructorInfo)this).getParameters() ) );
      }
      else if( this instanceof IMethodInfo)
      {
        featureInfo = type.getTypeInfo().getMethod( getDisplayName(), getParamTypes( ((IMethodInfo)this).getParameters() ) );
      }
      else if( this instanceof IPropertyInfo)
      {
        featureInfo = type.getTypeInfo().getProperty( getName() );
      }
      else
      {
        assert this instanceof ITypeInfo;
        featureInfo = type.getTypeInfo();
      }
      if( featureInfo != null )
      {
        if( featureInfo instanceof BaseFeatureInfo )
        {
          BaseFeatureInfo baseFeatureInfo = (BaseFeatureInfo)featureInfo;
          infos.add( baseFeatureInfo );
        }
      }
    }
  }

  public IType[] getParamTypes( IParameterInfo[] parameters )
  {
    List<IType> retValue = new ArrayList<IType>();
    if( parameters != null )
    {
      for( IParameterInfo parameterInfo : parameters )
      {
        retValue.add( parameterInfo.getFeatureType() );
      }
    }

    return retValue.toArray( new IType[retValue.size()] );
  }

  public BaseFeatureInfo( IFeatureInfo container )
  {
    _container = container;
  }

  public BaseFeatureInfo( IType intrType )
  {
    _intrType = intrType;
  }

  public IFeatureInfo getContainer()
  {
    return _container;
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getDescription()
  {
    return getName();
  }

  public IType getOwnersType()
  {
    IFeatureInfo container = getContainer();
    if( _intrType == null && container != null )
    {
      return container.getOwnersType();
    }
    return _intrType;
  }


  /**
   * Returns the list of annotations <b>exactly matching</b> the annotation passed in.  If the annotation is a sub type
   * of the type passed in, this will not return those annotations.
   * <p/>
   * This is equivilent to calling getAnnotations().get(type).
   *
   * @param type the type to look for
   */
  public List<IAnnotationInfo> getAnnotationsOfType( IType type )
  {
    return ANNOTATION_HELPER.getAnnotationsOfType( type, getAnnotations() );
  }

  public boolean hasAnnotation( IType type )
  {
    return ANNOTATION_HELPER.hasAnnotation( type, getAnnotations() );
  }

  @Override
  public IAnnotationInfo getAnnotation( IType type )
  {
    return ANNOTATION_HELPER.getAnnotation( type, getAnnotations(), type.getDisplayName() );
  }

  @Override
  public boolean hasDeclaredAnnotation( IType type )
  {
    return ANNOTATION_HELPER.hasAnnotation( type, getDeclaredAnnotations() );
  }

  public List<IAnnotationInfo> getAnnotations()
  {
    if( _annotations == null )
    {
      TypeSystem.lock();
      try
      {
        if( _annotations == null )
        {
          List<IAnnotationInfo> annotations = new ArrayList<IAnnotationInfo>();
          addAnnotations( annotations, this, new IdentityHashMap());
          _annotations = GosuCollectionUtil.compactAndLockList(annotations);
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return _annotations;
  }

  private void addAnnotations( List<IAnnotationInfo> annotations, BaseFeatureInfo fi, Map visitedFeatures )
  {
    if( !visitedFeatures.containsKey( fi ) )
    {
      for( IAnnotationInfo annotationInfo : fi.getDeclaredAnnotations() )
      {
        if( fi == this )
        {
          annotations.add( annotationInfo );
        }
        else if( ANNOTATION_HELPER.isInherited( annotationInfo.getType() ) &&
                 ANNOTATION_HELPER.shouldAddInheritedAnnotation( this, annotations, annotationInfo ) )
        {
          annotations.add( annotationInfo );
        }
      }
      visitedFeatures.put( fi, null );
      for( BaseFeatureInfo baseFeatureInfo : fi.getSuperAnnotatedElements() )
      {
        addAnnotations( annotations, baseFeatureInfo, visitedFeatures );
      }
    }
  }

  public boolean isVisible( IScriptabilityModifier constraint )
  {
    for( ScriptabilityModifier modifier : getScriptability() )
    {
      if( modifier.isVisible( constraint ) )
      {
        return true;
      }
    }
    return getScriptability().isEmpty();
  }

  public boolean isScriptable()
  {
    return isVisible( ScriptabilityModifiers.SCRIPTABLE );
  }

  public boolean isHidden()
  {
    try {
      final List<ScriptabilityModifier> scriptability = getScriptability();
      return !scriptability.isEmpty() &&
      scriptability.contains( ScriptabilityModifier.HIDDEN );
    } catch (Exception e) {
      return false;
    }
  }

  public boolean isScriptableTagPresent()
  {
    return !getScriptability().isEmpty();
  }

  private List<ScriptabilityModifier> getScriptability()
  {
    if( _scriptablity == null )
    {
      TypeSystem.lock();
      try
      {
        if( _scriptablity == null )
        {
          List<ScriptabilityModifier> scriptablity = Collections.emptyList();
          List<IAnnotationInfo> annotations = getAnnotationsOfType( TypeSystem.get( Scriptable.class ) );
          if( annotations != null && !annotations.isEmpty() )
          {
            // We just want to take the *first* scriptable annotaiton.  I.e. we allow absolute override.
            scriptablity = Arrays.asList(((Scriptable)CommonServices.getGosuIndustrialPark().evaluateAnnotation(annotations.get(0))).value());
            scriptablity = compactAndLockList(scriptablity);
          }
          _scriptablity = scriptablity;
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return _scriptablity;
  }

  public boolean isAbstract()
  {
    return false;
  }

  public boolean isFinal()
  {
    return false;
  }
  
  public boolean isDeprecated()
  {
    return !getDeprecatedAnnotation().isEmpty();
  }

  public String getDeprecatedReason()
  {
    List<IAnnotationInfo> annotation = getDeprecatedAnnotation();
    if (annotation.isEmpty()) {
      return null;
    } else {
      gw.lang.Deprecated value = (gw.lang.Deprecated) annotation.get(0).getInstance();
      return value.value();
    }
  }

  private List<IAnnotationInfo> getDeprecatedAnnotation() {
    if (_deprecated == null) {
      TypeSystem.lock();
      try {
        if (_deprecated == null) {
          _deprecated = getAnnotationsOfType(TypeSystem.getByFullName("gw.lang.Deprecated"));
        }
      }
      finally {
        TypeSystem.unlock();
      }
    }
    return _deprecated;
  }

  public boolean isPrivate()
  {
    return false;
  }

  public boolean isInternal()
  {
    return false;
  }

  public boolean isProtected()
  {
    return false;
  }

  public boolean isPublic()
  {
    return true;
  }

  public IType getActualTypeInContainer( IFeatureInfo container, IType type )
  {
    IType ownerType = container.getOwnersType();
    if( ownerType.isParameterizedType() )
    {
      TypeVarToTypeMap actualParamByVarName = TypeSystem.mapTypeByVarName( ownerType, ownerType, true );
      if( container instanceof IGenericMethodInfo )
      {
        for( IGenericTypeVariable tv : ((IGenericMethodInfo)container).getTypeVariables() )
        {
          if( actualParamByVarName.isEmpty() )
          {
            actualParamByVarName = new TypeVarToTypeMap();
          }
          actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), tv.getTypeVariableDefinition().getType() );
        }
        type = TypeSystem.getActualType( type, actualParamByVarName, true );
      }
    }
    return type;
  }

}
