package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IGosuProgram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public final class InstrumentationManager
{
  public static ThreadLocal<Boolean> IS_TESTER_DEBUGGER = new ThreadLocal<Boolean>();
  private static final Map<IRuntimeObserver,Integer> _observers =
    Collections.synchronizedMap( new LinkedHashMap<IRuntimeObserver,Integer>() );

  private static Set<String> _blacklist = generateBlacklist();
  private static Set<String> _packageBlacklist = generatePackageBlacklist();
  private static final boolean _forceInstrumentation = Boolean.getBoolean("gwdebug");

  private static final List<IBytecodeTransformer> _transformers =
    Collections.synchronizedList( new ArrayList() );

  private InstrumentationManager()
  {
    // Static-only access
  }

  public static void addRuntimeObserver( IRuntimeObserver observer )
  {
    synchronized( _observers )
    {
      Integer count = _observers.get( observer );
      _observers.put( observer, count == null ? 1 : count + 1 );
    }
  }

  public static void removeRuntimeObserver( IRuntimeObserver observer )
  {
    synchronized( _observers )
    {
      Integer count = _observers.get( observer );
      if( count != null )
      {
        if( count == 0 )
        {
          throw new IllegalStateException();
        }
        if( count == 1 )
        {
          _observers.remove( observer );
        }
        else
        {
          _observers.put( observer, count-1 );
        }
      }
    }
  }


  // Called via instrumentation
  @SuppressWarnings({"UnusedDeclaration"})
  public static void onBeforeExecute()
  {
    for( IRuntimeObserver observer : getObservers())
    {
      RuntimeInfoAtStatement ctx = getCtx();
      if( observer.observes( ctx.getEnclosingType() ) )
      {
        observer.onBeforeExecute( ctx );
      }
    }
  }

  private static Set<IRuntimeObserver> getObservers() {
    synchronized (_observers) {
      return new HashSet<IRuntimeObserver>(_observers.keySet());
    }
  }

  public static boolean shouldInstrument( ICompilableType gosuClass )
  {
    if( gosuClass instanceof IGosuProgram )
    {
      // Programs are not debuggable in studio
      return false;
    }
    if( _blacklist.contains( gosuClass.getName() ) ) {
      // some classes should never be instrumented, e.g. gw.internal.webservice.studio.StudioService
      return false;
    }
    if( _packageBlacklist.contains( gosuClass.getNamespace() )) {
      // some packages are not instrumented, e.g. gw.xml.ws.annotation.*
      return false;
    }
    if( _forceInstrumentation ) {
      return true;
    }
    for( IRuntimeObserver observer : getObservers())
    {
      if( observer.observes( gosuClass ) )
      {
        return true;
      }
    }
    return false;
  }

  public static RuntimeInfoAtStatement getCtx()
  {
    return RuntimeInfoAtStatement.getCtx();
  }

  public static boolean hasObservers() {
    return !_observers.isEmpty();
  }
  public static boolean isTesterDebugger() {
    Boolean b = IS_TESTER_DEBUGGER.get();
    return b == null ? false : b;
  }

  private static Set<String> generateBlacklist() {
    Set<String> blacklist = Collections.synchronizedSet(new HashSet<String>());
    blacklist.add( "gw.internal.webservice.studio.StudioService" );
    blacklist.add( "gw.lang.RpcWebService" );
    blacklist.add( "gw.lang.RpcWebServiceMethod" );
    blacklist.add( "gw.servlet.Servlet" );
    return blacklist;
  }

  private static Set<String> generatePackageBlacklist() {
    Set<String> packageBlacklist = Collections.synchronizedSet(new HashSet<String>());
    packageBlacklist.add( "gw.xml.ws");
    packageBlacklist.add( "gw.xml.ws.annotation");
    packageBlacklist.add( "gw.internal.xml.ws" );

    // PL-15357 - Workaround JVM crash caused by certain CC sample data class methods when debugging smoketests.
    // Can be removed if the cause of the crash (possibly malformed bytecode) is fixed.
    packageBlacklist.add( "gw.sampledata");
    return packageBlacklist;
  }

  public static void blacklistClass(String gosuClassName) {
    _blacklist.add(gosuClassName);
  }

  public static class RetryCompileException extends RuntimeException {
    public RetryCompileException(String gosuClassName, ClassFormatError ve) {
      super("Code size for " + gosuClassName + " is too large to support debug instrumentation.  Debugging this class will be disabled.", ve);
    }
  }

  public static void addBytecodeTransformer( IBytecodeTransformer transformer )
  {
    synchronized( _transformers )
    {
      _transformers.add( transformer );
    }
  }

  public static byte[] transform( IType type, byte[] bytecode )
  {
    if( !_transformers.isEmpty() )
    {
      synchronized( _transformers )
      {
        for( IBytecodeTransformer transformer : _transformers )
        {
          bytecode = transformer.transformBytes( type, bytecode );
        }
      }
    }
    return bytecode;
  }

  public interface IBytecodeTransformer
  {
    byte[] transformBytes( IType type, byte[] bytes );
  }
}
