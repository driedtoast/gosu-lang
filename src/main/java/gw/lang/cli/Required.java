package gw.lang.cli;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Inherited;


/**
 * Used to mark a property as being required from the command line
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Required
{
}
