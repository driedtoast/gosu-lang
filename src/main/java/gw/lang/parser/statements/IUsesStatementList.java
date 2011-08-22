package gw.lang.parser.statements;

import gw.lang.parser.IParsedElement;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IUsesStatementList extends IParsedElement {
  List<IUsesStatement> getUsesStatements();
}
