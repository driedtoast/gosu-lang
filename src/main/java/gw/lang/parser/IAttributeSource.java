package gw.lang.parser;

import gw.lang.reflect.ITypeLoaderListener;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IAttributeSource extends ITypeLoaderListener
{
  boolean hasAttribute( String strAttr );

  Object getAttribute( String strAttr );

  void setAttribute( String strAttr, Object value );
}
