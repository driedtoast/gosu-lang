package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IAnnotationInfoFactory
{
  IAnnotationInfo create( IType type, Object expressionValue, IFeatureInfo owner );
}