package gw.lang.cli;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Inherited;


/**
 * Gives a name to the argument for this property (default is "value").  This annotation
 * applies only to scalar (non-array) properties.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ArgNames
{
  public abstract String[] names();
}