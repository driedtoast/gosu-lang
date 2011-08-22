package gw.lang.parser;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface IUsageSiteValidatorReference
{
  /**
   * A reference to a class implementing the IUsageSiteValidator interface and a no-arg constructor
   */
  public abstract Class<? extends IUsageSiteValidator> value();
}