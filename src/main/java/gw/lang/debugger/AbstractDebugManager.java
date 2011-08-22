package gw.lang.debugger;

import gw.config.CommonServices;
import gw.lang.parser.IManagedContext;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.InstrumentationManager;
import gw.lang.parser.RuntimeInfoAtStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IGosuClass;
import gw.util.Stack;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class AbstractDebugManager implements IDebugManager
{
  private static boolean g_bDebuggable;
  private static Boolean g_retainDebugInfo;

  protected IDebugDriver _driver;
  protected final List<BreakPoint> _breakPoints;
  protected boolean _bDebugging;


  protected AbstractDebugManager()
  {
    _breakPoints = new ArrayList<BreakPoint>();
  }

  /**
   * Subclasses must implement this to provide their thread-local runtime symbol
   * table.
   *
   * @return The symbol table
   */
  abstract protected ISymbolTable getRuntimeSymbolTable();

  public void setDebugging( boolean bDebugging, IDebugDriver driver )
  {
    // Refresh the type system when we start debugging
    makeDebuggable();
    if( !_bDebugging && bDebugging )
    {
      TypeSystem.refresh( true );
    }
    _bDebugging = bDebugging;
    _driver = driver;
    if( bDebugging )
    {
      InstrumentationManager.addRuntimeObserver( this );
    }
    else
    {
      InstrumentationManager.removeRuntimeObserver( this );
    }
  }

  public IDebugDriver getDebugDriver() {
    return _driver;
  }

  private void makeDebuggable()
  {
    if( !isDebuggable() )
    {
      TypeSystem.refresh();
    }
    setDebuggable( true );
  }

  public boolean isDebugging()
  {
    return _bDebugging;
  }

  public void addBreakPoint( BreakPoint bp )
  {
    // Refresh the type system when we start debugging
    makeDebuggable();
    synchronized( _breakPoints )
    {
      _breakPoints.remove( bp ); // remove the old
      _breakPoints.add( bp );    // and the new
    }
  }

  public void removeBreakPoint( BreakPoint bp )
  {
    // Refresh the type system when we start debugging
    makeDebuggable();
    synchronized( _breakPoints )
    {
      _breakPoints.remove( bp );
    }
  }

  public List getBreakPoints()
  {
    // Refresh the type system when we start debugging
    makeDebuggable();
    synchronized( _breakPoints )
    {
      return Collections.unmodifiableList( _breakPoints );
    }
  }

  @Override
  public void onBeforeExecute( RuntimeInfoAtStatement ctx )
  {
    if( !isDebugging() )
    {
      return;
    }

    IType gsClass = TypeSystem.getCurrentCompilingType();
    if( gsClass instanceof IGosuClass )
    {
      return;
    }

    DebugLocationContext debugCtx = getDebugLocationContext( ctx );
    onBeforeExecute( debugCtx );
  }

  protected void onBeforeExecute( DebugLocationContext ctx )
  {
    if( !isDebugging() )
    {
      return;
    }

    if( !isDebuggable( ctx ) )
    {
      return;
    }

    if( !getThreadDebuggableInGosu() ) {
      return;
    }

    switch( _driver.getCommand() )
    {
      case CMD_RUN:
        if( isActiveBreakPoint( ctx ) )
        {
          _driver.onLocation( ctx );
        }
        break;

      case CMD_STEP_OVER:
        if( (!isSameLineNumber() &&
             !isDescendantCall()) ||
            isActiveBreakPoint( ctx ) )
        {
          _driver.onLocation( ctx );
        }
        break;

      case CMD_STEP_INTO:
        _driver.onLocation( ctx );
        break;
    }
  }

  protected boolean isDebuggable( DebugLocationContext ctx )
  {
    return ctx == null || ctx.getContext() == null || ctx.getContext().isDebuggable();
  }

  private boolean isSameLineNumber()
  {
    Stack current = RuntimeInfoAtStatement.getCallStack();
    Stack last = (Stack)_driver.getCallStack();
    return current.size() > 0 && last.size() > 0 && current.size() == last.size() &&
     ((RuntimeInfoAtStatement)current.peek()).getLineNumber() == ((RuntimeInfoAtStatement)last.peek()).getLineNumber();
  }

  /**
   * The idea is that a call is assumed to be descendant if it's call stack is
   * larger than the last call stack. Also if the stacks roots are not the same
   * we assume the call is descendant e.g., a call to a java method that in
   * turn invokes some gosu dynamically, as is the case for a lot of PCF
   * classes.
   *
   * @return True if call descends from current execution stack
   */
  private boolean isDescendantCall()
  {
    Stack current = RuntimeInfoAtStatement.getCallStack();
    Stack last = (Stack)_driver.getCallStack();
    return !areCallStackRootsSame( current, last ) ||
           current.size() > last.size();
  }

  private boolean areCallStackRootsSame( Stack current, Stack last )
  {
    RuntimeInfoAtStatement currentRoot = getRoot( current );
    RuntimeInfoAtStatement lastRoot = getRoot( last );
    return currentRoot == null || lastRoot == null
           ? currentRoot == lastRoot
           : currentRoot.getOrigin() == lastRoot.getOrigin();
  }

  private RuntimeInfoAtStatement getRoot( Stack currentLocationCallStack )
  {
    if( currentLocationCallStack != null && !currentLocationCallStack.isEmpty() )
    {
      return (RuntimeInfoAtStatement)currentLocationCallStack.get( 0 );
    }
    return null;
  }

  protected boolean isActiveBreakPoint( DebugLocationContext peLocationCtx )
  {
    synchronized( _breakPoints )
    {
      //noinspection ForLoopReplaceableByForEach
      for( int i = 0; i < _breakPoints.size(); i++ )
      {
        BreakPoint bp = _breakPoints.get( i );
        if( !BreakPoint.areBreakpointsMuted() && bp.isActive() && peLocationCtx.equals( bp.getContext() ) )
        {
          return true;
        }
      }

      return false;
    }
  }

  private DebugLocationContext getDebugLocationContext( RuntimeInfoAtStatement ctx )
  {
    IGosuClass aClass = (IGosuClass)ctx.getEnclosingType();
    if( aClass == null )
    {
      CommonServices.getEntityAccess().getLogger().error( "No enclosing type for ctx in " + ctx.getEnclosingFunction() );
    }
    IManagedContext context = aClass.getManagedContext();
    if( context == null )
    {
      CommonServices.getEntityAccess().getLogger().error( "No context for " + aClass.getName() );
    }
    IDebugContextProperties debugCtx = context.getContext();
    if( debugCtx == null )
    {
      CommonServices.getEntityAccess().getLogger().error( "No debugCtx context for " + aClass.getName() );
    }
    return new DebugLocationContext( debugCtx, ctx.getLineNumber(), 0 );
  }

  public static boolean isDebuggable()
  {
    if( g_retainDebugInfo == null )
    {
      // A production server can be configured to RetainDebugInfo, in which case debug info is never cleared. This
      // paves the way for a production server to be debuggable via studio without requiring a type sys refresh to
      // recompile and include debug info.
      g_retainDebugInfo = CommonServices.getEntityAccess().isRetainDebugInfo();
    }
    return g_bDebuggable || g_retainDebugInfo;
  }

  public static void setDebuggable( boolean bDebuggable )
  {
    g_bDebuggable = bDebuggable;
  }

  @Override
  public boolean observes( ICompilableType gsClass )
  {
    return isDebugging();
  }

  // Access to gw.lang.reflect.StudioServiceHelper in pl module.
  private Object _studioServiceHelper;
  private Method _getThreadDebuggable;

  private boolean getThreadDebuggableInGosu() {
    try {
      if(_getThreadDebuggable == null) {
        Class<?> studioServiceHelperClass = Class.forName("gw.lang.reflect.StudioServiceHelper");
        Method _getInstance = studioServiceHelperClass.getMethod("getInstance");
        _studioServiceHelper = _getInstance.invoke(null);
        _getThreadDebuggable = studioServiceHelperClass.getMethod("isSafeToDebug");
      }
      Boolean result = (Boolean) _getThreadDebuggable.invoke(_studioServiceHelper);
      return result.booleanValue();
    } catch(Exception ex) {
      return false;
    }
  }

}
