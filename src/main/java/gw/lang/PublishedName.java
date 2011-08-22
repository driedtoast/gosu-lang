package gw.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to override the property name of a getter or setter.
 * <p/>
 * By default the property name is the remaining parts after the get or set.  Properties are matched by comparing
 * the remaining parts and if they match, the matched set is the property.
 * <p/>
 * This annotation allows the name of the property to be changed.  It only works on properties and the matching
 * must occur before the property is renamed.
 * <p/>
 * The @PublishedName annotation will first be searched on the read method, then the write.  If on either the property is renamed
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface PublishedName
{
  public String value();
}
