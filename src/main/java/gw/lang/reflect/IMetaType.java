package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IMetaType extends INonLoadableType
{
  IType getType();

  boolean isLiteral();
}
