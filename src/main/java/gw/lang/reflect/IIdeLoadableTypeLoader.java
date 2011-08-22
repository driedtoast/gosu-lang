package gw.lang.reflect;

import gw.lang.UnstableAPI;

import java.util.Collection;

/**
 * Marker interface to indicate that a type loader should show its type in an IDE
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IIdeLoadableTypeLoader
{
  Collection<? extends CharSequence> getAllIdeLoadableTypeNames( boolean bInternalMode );
}
