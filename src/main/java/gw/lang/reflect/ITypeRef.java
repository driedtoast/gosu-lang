package gw.lang.reflect;

/**
 * !! This interface is not intended to be implemented by non-internal code.
 *
 * !! Only the internal type proxy is permitted to implement this interface.
 * 
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITypeRef extends IType
{
  boolean _shouldReload();

  void _clearType();

  void _setStale( boolean bStale );

  Class<? extends IType> _getClassOfRef();

  IMetaType getMetaType();

  IMetaType getLiteralType();
}
