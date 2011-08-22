package gw.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to override the published type of a property or constructor parameter. In the case of a property,
 * the annotation on the getter is used in preference to the annotation on the setter.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.PARAMETER } )
@Inherited
public @interface PublishedIType
{
  public abstract String value();
}