package gw.lang.ir.builder;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRStatement;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public abstract class IRStatementBuilder extends IRElementBuilder {
  private IRStatement _statement;

  public IRStatement build(IRBuilderContext context) {
    if (_statement == null) {
      _statement = buildImpl(context);
    }
    return _statement;
  }

  protected abstract IRStatement buildImpl(IRBuilderContext context);

}
