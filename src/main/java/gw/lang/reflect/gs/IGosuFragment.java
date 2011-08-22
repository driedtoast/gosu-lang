package gw.lang.reflect.gs;

import gw.lang.parser.IExpression;

/**
 * An IGosuFragment represents a light-weight wrapper around a Gosu expression or statement list.  Fragments
 * are handled separately from normal Gosu programs so that they can parse and compile much more efficiently.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuFragment extends ICompilableType {

  public static final String FRAGMENT_PACKAGE = "fragment_";

  Object evaluate(IExternalSymbolMap externalSymbols);

  Object evaluateRoot(IExternalSymbolMap externalSymbols);

  IExpression getExpression();

}
