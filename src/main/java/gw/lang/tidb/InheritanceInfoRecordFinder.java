package gw.lang.tidb;

import gw.lang.reflect.ITypeInfo;

import java.util.Set;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface InheritanceInfoRecordFinder
{
  /**
   * @return Set of supertypes (classes and interfaces) (or subtypes) of the given type info. The set contains all
   *         descendants. For only one level, see findDirectSupertypes() and findDirectSubtypes(). The version that takes a
   *         FeatureInfoID can be used when the type is not available, e.g. it has been deleted.
   */
  public Set<? extends IInheritanceRecord> findSupertypes( ITypeInfo typeInfo );

  public Set<? extends IInheritanceRecord> findSubtypes( ITypeInfo typeInfo );

  public Set<? extends IInheritanceRecord> findSupertypes( String typeInfoID );

  public Set<? extends IInheritanceRecord> findSubtypes( IFeatureInfoId typeInfoID );

  public Set<? extends IInheritanceRecord> findDirectSupertypes( ITypeInfo typeInfo );

  public Set<? extends IInheritanceRecord> findDirectSubtypes( ITypeInfo typeInfo );

  public Set<? extends IInheritanceRecord> findDirectSupertypes( IFeatureInfoId typeInfoID );

  public Set<? extends IInheritanceRecord> findDirectSubtypes( IFeatureInfoId typeInfoID );

}
