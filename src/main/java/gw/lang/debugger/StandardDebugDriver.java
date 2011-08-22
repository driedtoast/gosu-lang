package gw.lang.debugger;

import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IScriptabilityModifier;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class StandardDebugDriver extends AbstractDebugDriver
{
  public IDebugManager getDebugManager()
  {
    return StandardDebugManager.instance();
  }

  public ISymbolTable getSymbolTable()
  {
    return TypeSystem.getCompiledGosuClassSymbolTable();
  }

  public IScriptabilityModifier getVisibility()
  {
    return ScriptabilityModifiers.SCRIPTABLE;
  }

  protected boolean sessionMatchesUserId()
  {
    return true;
  }
}
