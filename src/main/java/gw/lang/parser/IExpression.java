package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IExternalSymbolMap;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IExpression extends IParsedElement, IHasType
{
  /**
   * Evaluates this Expression and returns the result.
   */
  Object evaluate();

  Object evaluate(IExternalSymbolMap externalSymbols);

  IType getContextType();

  boolean isNullSafe();
}
