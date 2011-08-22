package gw.lang.parser.statements;

import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IClassDeclaration extends IParsedElementWithAtLeastOneDeclaration
{
  CharSequence getClassName();

  IGosuClass getGSClass();
}
