package gw.lang.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * The AnnotationUsage meta-annotation is used to specify how an annotation can be used.
 * <p/>
 * An annotation usage takes two parameters, the usage target and the usage modifier.  The usage target specifies
 * where the annotation can live, i.e. on the class, constructor, property, or method.
 * <p/>
 * The modifier describes how many times an attribute can be used at the specified location.
 * <p/>
 * Annotation with no usage attributes defaults to Many annotations allowed at all targets.  As soon as one
 * AnnotationUsage is specified, all other targets default to None.  I.e. if you only specify that an annotation
 * can be used in the class, then it cannot be used in any other part of the class file.
 * <p/>
 * You can specify multiple AnnotationUsage annotations to allow an annotation to appear in more than one
 * part of a class file.
 * <p>
 *  Copyright 2010 Guidewire Software, Inc.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AnnotationUsage(target = UsageTarget.TypeTarget, usageModifier = UsageModifier.Many)
public @interface AnnotationUsage {
  UsageTarget target();
  UsageModifier usageModifier();
}
