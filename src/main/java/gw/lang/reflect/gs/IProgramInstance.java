package gw.lang.reflect.gs;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IProgramInstance
{
  public Object evaluate(IExternalSymbolMap symbols);

  public Object evaluateRootExpr(IExternalSymbolMap symbols);
}
