package gw.lang.annotation;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class modifier describes how this annotation can be used on classes.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public enum UsageModifier {
  /**
   * Use None to specify this annotation cannot exist on a class
   */
  None,
  /**
   * Use None to specify this annotation can only appear once on a class
   */
  One,
  /**
   * Use None to specify this annotation can appear many times on a class
   */
  Many;

  public static UsageModifier getUsageModifier( UsageTarget targetType, IType annotationType )
  {
    UsageModifier modifier = null;
    //First look for gosu-style usage annotations
    List<AnnotationUsage> usageInfos = getExplicitUsageAnnotations( annotationType );
    if( usageInfos != null && usageInfos.size() > 0 )
    {
      return getUsageModifier( targetType, modifier, usageInfos );
    }
    // if it's a java annotation with no explicit annotation usage, translate the java element type information
    else if( annotationType instanceof IJavaType &&
             TypeSystem.get( Annotation.class ).isAssignableFrom( annotationType ) )
    {
      return translateJavaElementTypeToUsageModifier( targetType, annotationType );
    }
    else
    {
      // By default, gosu annotations can appear multiple times
      return UsageModifier.Many;
    }
  }


  private static List<AnnotationUsage> getExplicitUsageAnnotations( IType annotationType )
  {
    ArrayList<AnnotationUsage> lst = new ArrayList<AnnotationUsage>();
    List<IAnnotationInfo> usageAnnotations = annotationType.getTypeInfo().getAnnotationsOfType( TypeSystem.get( AnnotationUsage.class ) );
    if( usageAnnotations != null )
    {
      for( IAnnotationInfo iAnnotationInfo : usageAnnotations )
      {
        lst.add( (AnnotationUsage)iAnnotationInfo.getInstance() );
      }
    }
    List<IAnnotationInfo> usagesAnnotations = annotationType.getTypeInfo().getAnnotationsOfType( TypeSystem.get( AnnotationUsages.class ) );
    if( usagesAnnotations != null )
    {
      for( IAnnotationInfo iAnnotationInfo : usagesAnnotations )
      {
        lst.addAll( Arrays.asList( ((AnnotationUsages)iAnnotationInfo.getInstance()).value() ) );
      }
    }
    return lst;
  }

  private static UsageModifier getUsageModifier( UsageTarget targetType, UsageModifier modifier, List<AnnotationUsage> usageInfos )
  {
    //If there are usages, then we must examine each one to find the one that most specifically applies
    for( AnnotationUsage usage : usageInfos )
    {
      // If usage applies to all, and we haven't had a more specific match yet, get the modifier for it
      if( usage.target().equals( UsageTarget.AllTarget ) && modifier == null  )
      {
        modifier = usage.usageModifier();
      }
      // If usage applies to the given target, it always overrides whatever else we've seen
      if( usage.target().equals( targetType ) )
      {
        modifier = usage.usageModifier();
      }
    }
    // if no usage matched, then that implies that the usage is None.
    if( modifier == null )
    {
      modifier = UsageModifier.None;
    }
    return modifier;
  }

  private static UsageModifier translateJavaElementTypeToUsageModifier( UsageTarget targetType, IType annotationType )
  {
    Target targetAnnotation = ((IJavaType)annotationType).getClassInfo().getAnnotation( Target.class );

    if( targetAnnotation == null || targetAnnotation.value().length == 0 )
    {
      return UsageModifier.One; // If there are no targets, it can be used everywhere
    }
    else
    {
      // otherwise, look for a target that matches our own UsageTarget
      for( ElementType elementType : targetAnnotation.value() )
      {
        UsageTarget usageTarget = null;
        if( elementType == ElementType.CONSTRUCTOR && targetType == UsageTarget.ConstructorTarget ||
            elementType == ElementType.FIELD && targetType == UsageTarget.PropertyTarget ||
            elementType == ElementType.ANNOTATION_TYPE && targetType == UsageTarget.TypeTarget ||
            elementType == ElementType.TYPE && targetType == UsageTarget.TypeTarget ||
            elementType == ElementType.METHOD && targetType == UsageTarget.MethodTarget )
        {
          return UsageModifier.One;
        }
      }
      return UsageModifier.None;
    }
  }


}
