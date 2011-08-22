package gw.lang.tidb;

import gw.lang.reflect.IFeatureInfo;

import java.util.Stack;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface IProvidesFeatureInfoId {
  IFeatureInfoId getFeatureId(Stack<IFeatureInfo> enclosingFeatureInfos);

  IFeatureInfo getOwningFeatureInfo();
}
