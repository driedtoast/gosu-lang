package gw.lang.cli;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Inherited;


/**
 * Used to give a property a short name from the command line
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ShortName
{
  public String name();
}