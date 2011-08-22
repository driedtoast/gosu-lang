package gw.lang.cli;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Inherited;


/**
 * Used to mark a property as being hidden from the command line (eg, do not show in the help messages)
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Hidden
{
}