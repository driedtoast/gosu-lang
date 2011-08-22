package gw.lang.debugger;

import gw.lang.parser.ISymbolTable;
import gw.lang.parser.InstrumentationManager;
import gw.lang.reflect.TypeSystem;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class StandardDebugManager extends AbstractDebugManager
{
  private static final StandardDebugManager INSTANCE = new StandardDebugManager();

  public static StandardDebugManager instance()
  {
    return INSTANCE;
  }

  public void shutdown() {
    IDebugDriver driver = getDebugDriver();
    if (driver instanceof AbstractDebugDriver) {
      ((AbstractDebugDriver)driver).stopDebugging();
    }
  }

  private StandardDebugManager()
  {
  }

  protected ISymbolTable getRuntimeSymbolTable()
  {
    return TypeSystem.getCompiledGosuClassSymbolTable();
  }

  @Override
  protected void onBeforeExecute(DebugLocationContext ctx) {
    if (!InstrumentationManager.isTesterDebugger()) {    
      super.onBeforeExecute(ctx);
    }
  }
}
