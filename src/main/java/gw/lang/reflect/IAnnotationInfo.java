package gw.lang.reflect;

import gw.util.GosuObjectUtil;
import gw.lang.annotation.IInherited;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.lang.annotation.Inherited;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IAnnotationInfo extends Serializable, IFeatureInfo
{
  Object getInstance();

  IType getType();
}
