package gw.lang.tidb;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface IInheritanceRecord extends IRecord<String>
{
  IFeatureInfoId getSupertypeTypeInfoID();

  IFeatureInfoId getSubtypeTypeInfoID();
}
