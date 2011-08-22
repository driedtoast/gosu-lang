package gw.lang.reflect.java;

import gw.lang.reflect.ITypeInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Allows a java class to specify a custom ITypeInfo implementation</p>
 *
 * <p>
 * The instantiation of the TypeInfo uses the following logic:
 * <ul>
 * <li>If the class has a constructor that takes an ITypeInfo, this constructor will be called with
 * the normal TypeInfo for the class</li>
 * <li>Otherwise, if an empty constructor is found, it will be invoked</li>
 * <li>Otherwise an IllegalStateException will be thrown</li>
 * </ul>
 * </p>
 *
 * <p>When implementing custom TypeInfo classes, it might be worth using gw.lang.reflect.java.CustomTypeInfoBase as a base
 * class and using gw.lang.reflect.MethodInfoBuilder and related classes to build the actual features.</p>
 *
 * <p>This annotation can be used for low-rent metaprogramming (e.g. exposing the content of a file in a type safe way)
 *    where a full custom typeloader might be overkill</p>
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CustomJavaTypeInfo
{
  Class<? extends ITypeInfo> value();
}
