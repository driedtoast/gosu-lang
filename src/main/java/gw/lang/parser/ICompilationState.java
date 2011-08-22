package gw.lang.parser;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ICompilationState
{
  boolean isCompilingHeader();

  boolean isHeaderCompiled();

  boolean isCompilingDeclarations();

  boolean isDeclarationsCompiled();

  boolean isCompilingDefinitions();

  boolean isDefinitionsCompiled();
}
