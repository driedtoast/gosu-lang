package gw.lang.shell;

import gw.lang.parser.ThreadSafeSymbolTable;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.reflect.TypeSystem;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class CompiledProgramSymbolTable extends ThreadSafeSymbolTable
{
  private final ThreadLocal<ISymbolTable> _symTable = new ThreadLocal<ISymbolTable>();

  private static volatile CompiledProgramSymbolTable INSTANCE;

  public static CompiledProgramSymbolTable instance()
  {
    if( INSTANCE == null )
    {
      TypeSystem.lock();
      try
      {
        if( INSTANCE == null )
        {
          INSTANCE = new CompiledProgramSymbolTable();
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return INSTANCE;
  }

  private CompiledProgramSymbolTable()
  {
    super( true );
  }

  protected ISymbolTable getThreadLocalSymbolTable()
  {
    ISymbolTable symTableCtx = _symTable.get();
    if( symTableCtx == null )
    {
      symTableCtx = new StandardSymbolTable( true );
      _symTable.set( symTableCtx );
    }
    return symTableCtx;
  }
}
