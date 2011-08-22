package gw.lang.reflect.gs;

import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.ISymbol;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IGenericMethodInfo;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuVarPropertyInfo extends IAttributedFeatureInfo, IPropertyInfo, IGenericMethodInfo
{
  IType getActualType( IVarStatement varStmt );

  boolean hasDeclaredProperty();

  boolean isScopedField();

  ISymbol getScopedSymbol();
}
