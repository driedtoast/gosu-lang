package gw.lang.debugger;

import gw.config.CommonServices;
import gw.lang.parser.ExternalSymbolMapForMap;
import gw.lang.parser.InstrumentationManager;
import gw.lang.parser.expressions.IProgram;
import gw.lang.parser.Keyword;
import gw.lang.parser.IStack;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IScope;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.parser.IActivationContext;
import gw.lang.parser.StandardScope;
import gw.lang.parser.ISymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.RuntimeInfoAtStatement;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.GosuShop;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.util.CaseInsensitiveHashMap;
import gw.util.StreamUtil;
import gw.util.Stack;

import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class AbstractDebugDriver implements IDebugDriver, IDebugService
{
  private static final String ARRAY_LENGTH_PREFIX = "length = ";
  private static final String[]
    EXCLUDE_SYMBOLS =
    {
      Keyword.KW_super.toString(),
      IStack.UNASSIGNED_LABEL,
      "print",
      "now",
    };

  private static final GosuClassDebugContextProperties UNKNOWN_CONTEXT =
          new GosuClassDebugContextProperties("UnknownContext", "unknown");

  private DebugLocationContext _onLocationContext;
  private ISymbolTable _suspendedSymbolTable;
  private IDebugDriver.DebugCommands _cmd;
  private boolean _bExecutionSuspended;
  private Iterable _callStack = new Stack<RuntimeInfoAtStatement>();
  private IGosuParser _parser;
  private IScope _visibleSymbols;
  private DebugExpression[] _establishedSymbols;
  private String _strUserName;
  private RunnableWithResult _task;
  private Stack<RuntimeInfoAtStatement> _suspendedCtxStack;

  /**
   * Override to indicate which debug manager is driving.
   */
  public abstract IDebugManager getDebugManager();

  /**
   * Lets have your symbol table.
   */
  public abstract ISymbolTable getSymbolTable();

  /**
   * Override to control typeinfo visibility in debugger ui stack frame, watches, etc.
   */
  public abstract IScriptabilityModifier getVisibility();


  public String getUserName()
  {
    return _strUserName;
  }
  public void setUserName( String strUserName )
  {
    _strUserName = strUserName == null ? strUserName : strUserName.trim();
  }

  public DebugLocationContext debug(IDebugDriver.DebugCommands iCmd, List<BreakPoint> breakpoints )
  {
    synchronized( this )
    {
      switch( iCmd )
      {
        case CMD_RUN:
        case CMD_STEP_INTO:
        case CMD_STEP_OVER:
          setCommand( iCmd );
          break;

        default:
          throw new RuntimeException( "Invalid debug command." );
      }

      IDebugManager debugManager = getDebugManager();
      debugManager.setDebugging( true, this );

      List oldBreakpoints = debugManager.getBreakPoints();
      for (Object oldBreakpoint : new ArrayList(oldBreakpoints)) {
        debugManager.removeBreakPoint((BreakPoint) oldBreakpoint);
      }
      for( BreakPoint bp : breakpoints )
      {
        debugManager.addBreakPoint( bp );
      }

      // Notify script execution to complete the command.
      notifyAll();
      try
      {
        // Wait for script execution to complete the command.
        waitForDebugCommandToComplete();
        return getDebugLocationContext();
      }
      catch( InterruptedException e )
      {
        stopDebugging();
        return null;
      }
    }
  }

  protected void waitForDebugCommandToComplete() throws InterruptedException
  {
    synchronized( this )
    {
      wait();
    }
  }

  public void stopDebugging()
  {
    synchronized( this )
    {
      getDebugManager().setDebugging( false, this );
      notifyAll();
    }
    try
    {
      // Wait a bit. Apache Axis is not thread safe on the client side, so we
      // have to take care to ensure that we don't cause the "stop" command and
      // the "debug" command to return simultaneously.
      Thread.sleep( 500 );
    }
    catch( InterruptedException e )
    {
      throw new RuntimeException( e );
    }
  }

  public void addBreakPoint( BreakPoint breakPoint )
  {
    getDebugManager().addBreakPoint( breakPoint );
  }

  public void removeBreakPoint( BreakPoint breakPoint )
  {
    getDebugManager().removeBreakPoint( breakPoint );
  }

  public boolean areBreakpointsMuted()
  {
    return BreakPoint.areBreakpointsMuted();
  }

  public void setBreakpointsMuted( boolean bMuted )
  {
    BreakPoint.setBreakpointsMuted( bMuted );
  }

  public DebugExpression[] establishSymbols( final int iScopeIndex, final int iPrivateGlobalScopeIndex )
  {
    return (DebugExpression[])
      runInSuspendedExectionThread(
        new RunnableWithResult()
        {
          public Object executeTask()
          {
            TypeSystem.pushIncludeAll();
            try
            {
              _establishedSymbols = makeSymbols( iScopeIndex, iPrivateGlobalScopeIndex );
              return _establishedSymbols;
            }
            finally
            {
              TypeSystem.popIncludeAll();
            }
          }
        } );
  }

  public DebugExpression[] getEstablishedSymbols()
  {
    return _establishedSymbols;
  }

  public DebugExpression[] evaluate( final String[] astrExpression )
  {
    return (DebugExpression[])
    runInSuspendedExectionThread(
      new RunnableWithResult()
      {
        public Object executeTask()
        {
          TypeSystem.pushIncludeAll();
          try
          {
            return eval( astrExpression );
          }
          finally
          {
            TypeSystem.popIncludeAll();
          }
        }
      } );
  }

  public String[] evaluate( final String strScript )
  {
    return (String[])
      runInSuspendedExectionThread(
        new RunnableWithResult()
        {
          public Object executeTask()
          {
            TypeSystem.pushIncludeAll();
            InstrumentationManager.IS_TESTER_DEBUGGER.set(true);
            try
            {
              return evaluateOrExecute( strScript );
            }
            finally
            {
              InstrumentationManager.IS_TESTER_DEBUGGER.set(false);
              TypeSystem.popIncludeAll();
            }
          }
        } );
  }

  public String executeTemplate( final String strTemplate )
  {
    return (String)
    runInSuspendedExectionThread(
      new RunnableWithResult()
      {
        public Object executeTask()
        {
          TypeSystem.pushIncludeAll();
          try
          {
            return executeTemplateNow( strTemplate );
          }
          finally
          {
            TypeSystem.popIncludeAll();
          }
        }
      } );
  }

  public IActivationContext[] getActivationContextStack()
  {
    return (IActivationContext[])
      runInSuspendedExectionThread(
        new RunnableWithResult()
        {
          public Object executeTask()
          {
            Stack<RuntimeInfoAtStatement> callStack = RuntimeInfoAtStatement.getCallStack();
            if( callStack == null )
            {
              return new RemoteActivationContext[0];
            }

            int iCallStackSize = callStack.size();
            IActivationContext[] activationCtxStack = new IActivationContext[iCallStackSize];
            for( int i = 0; i < iCallStackSize; i++ )
            {
              RuntimeInfoAtStatement ctx = callStack.get( i );
              DebugLocationContext debugCtx;
              String stackDisplay;
              if(ctx.getEnclosingType() != null) {
                debugCtx = new DebugLocationContext( ((IGosuClass)ctx.getEnclosingType()).getManagedContext().getContext(), ctx.getLineNumber(), 0 );
                stackDisplay = ctx.getStackDisplay();
              } else {
                debugCtx = new DebugLocationContext( UNKNOWN_CONTEXT, 0, 0);
                stackDisplay = "Unknown Context";
                if(i > 0 && activationCtxStack[iCallStackSize - i] != null) {
                  IActivationContext priorCtx = activationCtxStack[iCallStackSize - i];
                  if(priorCtx.getDebugContext().getContext() != UNKNOWN_CONTEXT) {
                    System.out.println("ERROR: Invalid debug context generated at or around " + priorCtx.getLabel());
                  }
                }
              }
              activationCtxStack[iCallStackSize-1-i] = new RemoteActivationContext( debugCtx, i, stackDisplay );
            }
            return activationCtxStack;
          }
        } );
  }

  //----------------------------------------------------------------------------
  // -- IDebugDriver impl --

  public void onLocation( DebugLocationContext locationContext )
  {
    synchronized( this )
    {
      if( !getDebugManager().isDebugging() )
      {
        return;
      }

      if( isExecutionSuspended() )
      {
        // There is already a suspended script execution thread being debugged.
        // We can only debug one thread at a time, so let other threads freely
        // execute as to not clobber the current debug session.
        return;
      }

      if( !sessionMatchesUserId() )
      {
        return;
      }

      setExecutionSuspended( true );

      setDebugLocationCtx( locationContext );
      setSuspendedSymbolTable( new Stack<RuntimeInfoAtStatement>( RuntimeInfoAtStatement.getCallStack() ),
                               makeSymbolTableFromCtxStack( -1 ) );

      // Notify the debugger of the completed command and current location.
      notifyAll();
      try
      {
        // Wait for the debugger to notify with a new command e.g., wait for a step instruction.
        do
        {
          wait();
          RunnableWithResult task = getTask();
          if( task == null )
          {
            break;
          }
          task.run();
          notifyAll();
        } while( true );
        setStackTrace();
        setSuspendedSymbolTable( null, null );
        setDebugLocationCtx( null );
        setExecutionSuspended( false );
      }
      catch( InterruptedException e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  private ISymbolTable makeSymbolTableFromCtxStack( int iScopeIndex )
  {
    StandardSymbolTable symTable = new StandardSymbolTable( true );
    Stack<RuntimeInfoAtStatement> ctxStack = RuntimeInfoAtStatement.getCallStack();
    if( iScopeIndex < 0 )
    {
      iScopeIndex = ctxStack.size()-1;
    }
    if( ctxStack != null )
    {
      RuntimeInfoAtStatement ctx = ctxStack.get( iScopeIndex );
      for( ISymbol sym : ctx.getLocalSymbols() )
      {
        symTable.putSymbol( sym );
      }
    }
    return symTable;
  }

  private Object runInSuspendedExectionThread( RunnableWithResult task )
  {
    synchronized( this )
    {
      if( !isExecutionSuspended() )
      {
        return task.run();
      }
      else
      {
        setTask( task );

        // Notify suspended execution thread of the task.
        notifyAll();
        try
        {
          do
          {
            // Wait for suspended execution thread to execute the task.
            wait();
          }while( !task.isFinishedRunning() );
          setTask( null );
          Throwable error = task.getError();
          if( error != null )
          {
            throw new RuntimeException( error );
          }
          return task.getResult();
        }
        catch( InterruptedException e )
        {
          throw new RuntimeException( e );
        }
      }
    }
  }

  abstract protected boolean sessionMatchesUserId();

  public Iterable getCallStack()
  {
    return _callStack;
  }

  public IDebugDriver.DebugCommands getCommand()
  {
    if( _cmd == null )
    {
      throw new RuntimeException( "Command not set." );
    }
    return _cmd;
  }


  //----------------------------------------------------------------------------
  // -- private methods --

  private IScope getVisibleSymbols()
  {
    return _visibleSymbols;
  }
  private void setVisibleSymbols( IScope visibleSymbols )
  {
    _visibleSymbols = visibleSymbols;
  }

  private void setStackTrace()
  {
    Stack<RuntimeInfoAtStatement> callStack = RuntimeInfoAtStatement.getCallStack();
    Stack<RuntimeInfoAtStatement> copy = new Stack<RuntimeInfoAtStatement>();
    for( RuntimeInfoAtStatement e : callStack )
    {
      copy.push( e.copy() );
    }
    _callStack = copy;
  }

  private void setCommand(IDebugDriver.DebugCommands iCommand )
  {
    _cmd = iCommand;
  }

  /*
   * Tracks if script execution is suspended or not. Script exec is suspended when
   * waiting for the debugger to issue a command e.g., step.
   */
  private boolean isExecutionSuspended()
  {
    return _bExecutionSuspended;
  }
  private void setExecutionSuspended( boolean bExecutionSuspended )
  {
    _bExecutionSuspended = bExecutionSuspended;
  }

  private void setDebugLocationCtx( DebugLocationContext locationContext )
  {
    _onLocationContext = locationContext;
  }
  private DebugLocationContext getDebugLocationContext()
  {
    return _onLocationContext;
  }

  private ISymbolTable getSuspendedSymbolTable()
  {
    return getSuspendedSymbolTable( -1 );
  }
  private ISymbolTable getSuspendedSymbolTable( int iScopeIndex )
  {
    if( iScopeIndex >= 0 )
    {
      setSuspendedSymbolTable( _suspendedCtxStack, makeSymbolTableFromCtxStack( iScopeIndex ) );
    }
    return _suspendedSymbolTable;
  }
  private void setSuspendedSymbolTable( Stack<RuntimeInfoAtStatement> ctxStack, ISymbolTable suspendedSymbolTable )
  {
    _suspendedCtxStack = ctxStack;
    _suspendedSymbolTable = suspendedSymbolTable;
  }

  private DebugExpression[] makeSymbols( int iScopeIndex, int iPrivageGlobalIndex )
  {
    ISymbolTable symTable = getSuspendedSymbolTable( iScopeIndex );
    if( symTable == null )
    {
      return new DebugExpression[0];
    }

    Collection mapSymbols = symTable.getSymbols().values();
    List listDebugSymbols = new ArrayList();
    setVisibleSymbols( new StandardScope() );
    for( Object listSymbol : mapSymbols )
    {
      ISymbol symbol = (ISymbol)listSymbol;
      if( isExcluded( symbol ) )
      {
        continue;
      }
      getVisibleSymbols().put( symbol.getCaseInsensitiveName(), symbol );
      IType type = symbol.getType();
      DebugExpression debugSymbol;
      if( type instanceof IFunctionType )
      {
        debugSymbol = new DebugExpression( symbol.getName(), symbol.getType(), null );
      }
      else
      {
        TypeSystem.getCompiledGosuClassSymbolTable().setCurrentIsolatedScope( iScopeIndex );
        try
        {
          debugSymbol = eval( symbol.getName(), symbol.getName() );
        }
        finally
        {
          TypeSystem.getCompiledGosuClassSymbolTable().getStack().setCurrentScopeIndexForDebugger( -1 );
        }
      }
      listDebugSymbols.add( debugSymbol );
    }
    return (DebugExpression[])listDebugSymbols.toArray( new DebugExpression[listDebugSymbols.size()] );
  }

  private boolean isExcluded( ISymbol symbol )
  {
    for (String EXCLUDE_SYMBOL : EXCLUDE_SYMBOLS) {
      if (EXCLUDE_SYMBOL.equalsIgnoreCase(symbol.getName())) {
        return true;
      }
    }
    return false;
  }

  private DebugExpression[] eval( String[] astrExpression )
  {
    if( astrExpression == null )
    {
      return null;
    }

    DebugExpression[] astrValues = new DebugExpression[astrExpression.length];
    for( int i = 0; i < astrExpression.length; i++ )
    {
      astrValues[i] = eval( astrExpression[i] );
    }

    return astrValues;
  }

  /**
   * Evaluate a Gosu expression or program.
   *
   * @param strExpression A Gosu expression or program.
   * @return The value of the expression (or return value of the program).
   */
  private DebugExpression eval( String strExpression )
  {
    return eval( strExpression, strExpression );
  }

  private DebugExpression eval( String strName, String strExpression )
  {
    initParser();

    IExpression expression;
    try
    {
      expression = parseExpression( strExpression );
    }
    catch( Exception e )
    {
      try
      {
        expression = parseProgram( strExpression );
      }
      catch( Exception e2 )
      {
        return getExceptionDebugExpression( e2, strName );
      }
    }

    // Note we need to push the scope here too because the call to expression.getType()
    // (esp. for Identifier expressions) needs to access the relative symbols which may
    // shadow what is otherwise in the symbol table.
    _parser.getSymbolTable().pushScope( getVisibleSymbols() );
    try
    {
      if( strName.equals( strExpression ) )
      {
        // Format the expression (e.g., for watch expressions)

        IType type = expression.getType();
        if( type.isArray() || type == IJavaType.STRING )
        {
          if( type.isArray() )
          {
            strExpression = "\"{\" + " + strExpression + " + \"} \" + " + "\"" + ARRAY_LENGTH_PREFIX + "\" + " + strExpression + ".length";
          }
          else if( type == IJavaType.STRING )
          {
            strExpression = "\"\\\"\" + " + strExpression + " + \"\\\"\"";
          }

          DebugExpression rde = eval( strName, strExpression );
          rde.setType( type );
          return rde;
        }
      }
      //noinspection unchecked
      String strValue = makeStringValue( expression.evaluate( new ExternalSymbolMapForMap( (CaseInsensitiveHashMap)getVisibleSymbols() ) ) );
      return new DebugExpression( strName, expression.getType(), strValue );
    }
    catch( Throwable e )
    {
      return getExceptionDebugExpression( e, strName );
    }
    finally
    {
      _parser.getSymbolTable().popScope();
    }
  }

  private DebugExpression getExceptionDebugExpression( Throwable e, String strName )
  {
    String strMsg = e.getMessage();
    if( strMsg == null )
    {
      strMsg = e.getClass().getName();
    }
    return new DebugExpression( strName, IGosuParser.NULL_TYPE, strMsg );
  }

  private String makeStringValue( Object value )
  {
    if( value instanceof Date )
    {
      DateFormat formatter = DateFormat.getDateTimeInstance( DateFormat.LONG, DateFormat.LONG );
      return formatter.format( (Date)value );
    }
    return CommonServices.getCoercionManager().makeStringFrom( value );
  }

  private void initParser()
  {
    if( _parser == null )
    {
      _parser = GosuParserFactory.createParser( getSuspendedSymbolTable(), getVisibility() );
    }
    else
    {
      _parser.setSymbolTable( getSuspendedSymbolTable() );
    }
  }

  private String[] evaluateOrExecute( String strScript )
  {
    initParser();

    PrintStream sysout = System.out;
    PrintStream syserr = System.err;

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream ps;
    try
    {
      ps = new PrintStream( out, false, "UTF-8" );
    } catch ( UnsupportedEncodingException e ) {
      throw new RuntimeException( e ); // shouldn't happen with UTF-8
    }
    System.setOut( ps );
    System.setErr( ps );

    IExpression expression = null;
    try
    {
      expression = parseExpression( strScript );
    }
    catch( Exception e )
    {
      try
      {
        expression = parseProgram( strScript );
      }
      catch( Exception e2 )
      {
        e2.printStackTrace();
      }
    }

    Object ret = null;
    _parser.getSymbolTable().pushScope( getVisibleSymbols() );
    try
    {
      assert expression != null;
      //noinspection unchecked
      ret = expression.evaluate( new ExternalSymbolMapForMap( (CaseInsensitiveHashMap)getVisibleSymbols() ) );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    finally
    {
      _parser.getSymbolTable().popScope();
      ps.flush();
      System.setOut( sysout );
      System.setErr( syserr );
    }

    return new String[]
    {
      (String) CommonServices.getCoercionManager().convertValue(ret, IJavaType.STRING),
      StreamUtil.toString( out.toByteArray() ),
    };
  }

  /**
   * Parse a Gosu expression.
   *
   * @param strExpression A Gosu expression.
   * @return The compiled expression.
   * @throws gw.lang.parser.exceptions.ParseResultsException
   *
   */
  private IExpression parseExpression( String strExpression ) throws ParseResultsException
  {
    _parser.getSymbolTable().pushScope( getVisibleSymbols() );
    try
    {
      _parser.setScript( strExpression );
      return _parser.parseExp( null );
    }
    finally
    {
      _parser.getSymbolTable().popScope();
    }
  }

  /**
   * Compile a Gosu program.
   *
   * @param strProgram A Gosu program.
   * @return The compiled program.
   * @throws gw.lang.parser.exceptions.ParseResultsException
   */
  private IProgram parseProgram( String strProgram ) throws ParseResultsException
  {
    _parser.getSymbolTable().pushScope( getVisibleSymbols() );
    try
    {
      _parser.setScript( strProgram );
      return _parser.parseProgram( null );
    }
    finally
    {
      _parser.getSymbolTable().popScope();
    }
  }

  private String executeTemplateNow( String strTemplate )
  {
    initParser();
    StringWriter writer = new StringWriter();
    _parser.getSymbolTable().pushScope( getVisibleSymbols() );
    try
    {
      GosuShop.generateTemplate( new StringReader( strTemplate ), writer, _parser.getSymbolTable() );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    finally
    {
      _parser.getSymbolTable().popScope();
    }
    return writer.toString();
  }

  private RunnableWithResult getTask()
  {
    return _task;
  }

  private void setTask( RunnableWithResult task )
  {
    _task = task;
  }
}
