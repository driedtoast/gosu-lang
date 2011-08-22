package gw.lang.parser;

import gw.lang.reflect.gs.IExternalSymbolMap;
import gw.lang.parser.statements.ITerminalStatement;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IStatement extends IParsedElement
{
  /**
   * Execute this statement.
   */
  Object execute();

  /**
   * Execute this statement.
   */
  Object execute(IExternalSymbolMap externalSymbols);

  boolean hasContent();

  /**
   * Indicates whether or not control flow is terminal at this statement.
   */
  ITerminalStatement getLeastSignificantTerminalStatement();
}
