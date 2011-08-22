package gw.lang;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Add this annotation to publish your class to gosu.
 * <p/>
 * The rules are as follows
 * <ul>
 * <li>If the PublishInGosu annotation is not present, the class is published according the the BeanInfo rules</li>
 * <li>If the annotation is present then a type info class is created.</li>
 * <li>If the annotation is present then all methods explicitly marked as @Scriptable will be published.
 * If the @Scriptable annotation is not present then the method is considered hidden</li>
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PublishInGosu
{
}
