package gw.lang.reflect.gs;

import gw.lang.reflect.IType;

/**
 * FragmentInstance serves as the base class for the classes that are generated for IGosuFragments.  Subclasses
 * will always implement the evaluate(IExternalSymbolMap) method, and may (where appropriate) also override
 * the evaluateRootExpression(IExternalSymbolMap) interface.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class FragmentInstance implements IGosuObject {

  public abstract Object evaluate(IExternalSymbolMap symbols);

  @SuppressWarnings({"UnusedDeclaration"})
  public Object evaluateRootExpression(IExternalSymbolMap symbols) {
    return null;
  }

  @Override
  public IType getIntrinsicType() {
    return null;
  }
}
