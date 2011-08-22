package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITypeRefFactory
{
  ITypeRef create( IType type );
  ITypeRef get( IType type );
  ITypeRef get( String strTypeName );
  ITypeRef getCaseInsensitive( String strTypeName );

  Class<? extends ITypeRef> getOrCreateTypeProxy( String strTypeClass );

  void remove(String fullyQualifiedTypeName);

  void clearCaches();

  boolean isClearing();
}
