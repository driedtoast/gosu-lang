package gw.lang.reflect;

import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.gs.IGosuPropertyInfo;
import gw.lang.reflect.gs.IGosuConstructorInfo;
import gw.lang.reflect.gs.IGosuClassTypeInfo;
import gw.lang.reflect.gs.IGosuMethodParamInfo;
import gw.lang.Throws;
import gw.lang.Returns;
import gw.lang.Scriptable;
import gw.lang.Param;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.annotation.ScriptabilityModifier;

import java.util.List;
import java.util.ArrayList;

/**
 * This class will be removed by the end of Diamond
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@Deprecated
public class AnnotationReflectUtil
{
  public static String evalDeprecationReason( IAnnotatedFeatureInfo featureInfo )
  {
    if( isGosuFeature( featureInfo ) )
    {
      String deprecatedReason = "";
      List<IAnnotationInfo> annotation = featureInfo.getAnnotationsOfType( TypeSystem.get( gw.lang.Deprecated.class ) );
      if( annotation.size() > 0 )
      {
        try
        {
          gw.lang.Deprecated deprecated = (gw.lang.Deprecated)ReflectUtil.evaluateAnnotation( annotation.get( 0 ) );
          if( deprecated != null )
          {
            deprecatedReason = deprecated.value();
          }
        }
        catch( Exception e )
        {
          //ignore
        }
      }
      return deprecatedReason;
    }
    else
    {
      return featureInfo.getDeprecatedReason();
    }
  }

  public static List<IExceptionInfo> evalThrowsInfos( IAnnotatedFeatureInfo typeInfo )
  {
    ArrayList<IExceptionInfo> lst = new ArrayList<IExceptionInfo>();
    for( Throws aThrows : evalThrows( typeInfo ) )
    {
      lst.add( new SyntheticExceptionInfo( typeInfo, aThrows.getExceptionType().getName(), aThrows.getExceptionDescription() ) );
    }
    return lst;
  }

  public static List<Throws> evalThrows( IAnnotatedFeatureInfo typeInfo )
  {
    List<Throws> throwsLst = new ArrayList<Throws>();
    try
    {
      for( IAnnotationInfo annotationInfo : typeInfo.getAnnotationsOfType( TypeSystem.get( Throws.class ) ) )
      {
        Throws aThrows = (Throws)(isGosuFeature( typeInfo ) ? ReflectUtil.evaluateAnnotation( annotationInfo ) : annotationInfo.getInstance());
        if( aThrows != null )
        {
          throwsLst.add( aThrows );
        }
      }
    }
    catch( Exception e )
    {
      //ignore
    }
    return throwsLst;
  }

  public static String evalReturnDescription( IMethodInfo method )
  {
    if( method instanceof IGosuMethodInfo )
    {
      List<IAnnotationInfo> annotation = method.getAnnotationsOfType( TypeSystem.getByFullName( "gw.lang.Returns" ) );
      if( annotation.size() > 0 )
      {
        if( isGosuFeature( method ) )
        {
          return ((Returns)ReflectUtil.evaluateAnnotation( annotation.get( 0 ) )).value();
        }
        else
        {
          return ((Returns)annotation.get( 0 ).getInstance()).value();
        }
      }
    }
    else
    {
      return method.getReturnDescription();
    }
    return "";
  }

  public static boolean evalVisibleForWebservice( IMethodInfo method )
  {
    // equivelent to method.isVisible( ScriptabilityModifiers.SCRIPTABLE_WEBSERVICE)
    List<IAnnotationInfo> annotation = method.getAnnotationsOfType( TypeSystem.get( Scriptable.class ) );
    if( annotation == null || annotation.size() == 0 )
    {
      return !evalIsHidden( method.getOwnersType().getTypeInfo() );
    }
    for( IAnnotationInfo annotationInfo : annotation )
    {
      Scriptable o = (Scriptable)ReflectUtil.evaluateAnnotation( annotationInfo );
      for( ScriptabilityModifier scriptabilityModifier : o.value() )
      {
        if( scriptabilityModifier.isVisible( ScriptabilityModifiers.SCRIPTABLE_WEBSERVICE ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean evalIsHidden( IAnnotatedFeatureInfo featureInfo )
  {
    // equivelent to method.isVisible( ScriptabilityModifiers.SCRIPTABLE_WEBSERVICE)
    for( IAnnotationInfo annotationInfo : featureInfo.getAnnotationsOfType( TypeSystem.get( Scriptable.class ) ) )
    {
      Scriptable o = (Scriptable)ReflectUtil.evaluateAnnotation( annotationInfo );
      for( ScriptabilityModifier scriptabilityModifier : o.value() )
      {
        if( scriptabilityModifier.equals( ScriptabilityModifier.HIDDEN ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  public static String evalParameterDecription( IParameterInfo parameterData )
  {
    // replicates logic in gw.internal.gosu.parser.GosuMethodParamInfo._description
    if( parameterData instanceof IGosuMethodParamInfo )
    {
      IFeatureInfo featureInfo = parameterData.getContainer();
      IAttributedFeatureInfo annotatedFI = (IAttributedFeatureInfo)featureInfo;
      List<IAnnotationInfo> annotation = annotatedFI.getAnnotationsOfType( TypeSystem.get( Param.class ) );
      for( IAnnotationInfo annotationInfo : annotation )
      {
        Param o = (Param)ReflectUtil.evaluateAnnotation( annotationInfo );
        if( o != null && o.getFieldName().equals( parameterData.getName() ) )
        {
          return o.getFieldDescription();
        }
      }
      return "";
    }
    else
    {
      return parameterData.getDescription();
    }
  }

  private static boolean isGosuFeature( IAnnotatedFeatureInfo featureInfo )
  {
    return featureInfo instanceof IGosuMethodInfo ||
           featureInfo instanceof IGosuPropertyInfo ||
           featureInfo instanceof IGosuConstructorInfo ||
           featureInfo instanceof IGosuClassTypeInfo;
  }

  public static class SyntheticExceptionInfo implements IExceptionInfo
  {
    private IFeatureInfo _container;
    private String _exceptionName;
    private String _exceptionDescription;

    public SyntheticExceptionInfo( IFeatureInfo container, String exceptionName, String exceptionDescription )
    {
      _container = container;
      _exceptionName = exceptionName;
      _exceptionDescription = exceptionDescription;
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
      return _exceptionName;
    }

    public String getDisplayName()
    {
      return _exceptionName;
    }

    public String getDescription()
    {
      return _exceptionDescription;
    }

    public IType getExceptionType()
    {
      return TypeSystem.getByFullNameIfValid( getName() );
    }
  }
}
