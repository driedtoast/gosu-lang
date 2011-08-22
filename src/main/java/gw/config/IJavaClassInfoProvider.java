/**
 * pCopyrightp
 */

package gw.config;

import java.util.Set;

import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.module.IModule;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaClassInfoProvider extends IService {
  IJavaClassInfo getJavaClassInfo(Class jClass);

  IJavaClassInfo getJavaClassInfo(String fullyQualifiedName, IModule module);

  Set<? extends CharSequence> getAllTypeNames(IModule module);
}