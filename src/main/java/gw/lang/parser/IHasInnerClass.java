package gw.lang.parser;

import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IHasInnerClass 
{
  /**
   * returns the appropriate inner class
   * @param strTypeName
   * @return
   */
  public IType getInnerClass( CharSequence strTypeName );
}
