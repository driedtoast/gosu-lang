package gw.lang.reflect;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IDerivableTypeLoader extends ITypeLoader {
  List<IType> getDerivedTypes(IType type);
}