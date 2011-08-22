package gw.lang.reflect.gs;

import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IEnhancementIndex
{
  boolean isTypeEnhanced( String strTypeName );

  void resetIndexes();

  void addEnhancementMethods( IType typeToEnhance, Collection<IMethodInfo> methodsToAddTo );

  void addEnhancementProperties(IType typeToEnhance, Map<CharSequence, IPropertyInfo> propertyInfosToAddTo, boolean caseSensitive);

  void maybeLoadEnhancementIndex();

  List<? extends IGosuEnhancement> getEnhancementsForType( IType gosuClass );

  List<? extends IGosuEnhancement> getEnhancementsForGenericType( IType gosuClass );

  void removeEntry( IGosuEnhancement enhancement );

  void addEntry( IType enhancedType, IGosuEnhancement enhancement );
}
