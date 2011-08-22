package gw.lang.parser.template;

import gw.lang.parser.ISymbolTable;

import java.io.Writer;
import java.io.Reader;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITemplateHost
{
  void pushScope();

  void popScope();

  void putSymbol( String name, Class type, Object value );

  ITemplateGenerator getTemplate( Reader templateReader );
  ITemplateGenerator getTemplate( Reader templateReader, String strFqn );

  void executeTemplate( ITemplateGenerator template, Writer writer );
  void executeTemplate( Reader inputStreamReader, Writer resultsStr );

  ISymbolTable getSymbolTable();
}
