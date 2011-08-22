package gw.lang;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Deprecated {
  public String value();
}
