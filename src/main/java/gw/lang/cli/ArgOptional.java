package gw.lang.cli;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Inherited;


/**
 * Flags a property as not needing an argument.  If this annotation is set on the property
 * and no value is passed in, it will be equivalent to passing an empty string in on the command
 * line.  This property only applies to non-boolean scalars (non-arrays)
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ArgOptional
{
}