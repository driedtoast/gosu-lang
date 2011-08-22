package gw.lang.parser;

import gw.lang.parser.expressions.IProgram;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.debugger.AbstractDebugManager;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class PostCompilationAnalysis
{
  private static final ThreadLocal<Boolean> isAnalysisThread = new ThreadLocal<Boolean>();

  public static boolean shouldAnalyze()
  {
    Boolean isAnalysisThread = PostCompilationAnalysis.isAnalysisThread.get();
    return isAnalysisThread != null && isAnalysisThread.booleanValue();
  }

  public static void setAnalysisThread()
  {
    if( AbstractDebugManager.isDebuggable() )
    {
      isAnalysisThread.set( true );
    }
    else
    {
      throw new IllegalStateException( "Can only perform post compilation analysis if debuggable" );
    }
  }

  public static void unsetAnalysisThread()
  {
    isAnalysisThread.set( false );
  }

  /**
   * Perform post compilation analysis on the given ParsedElement. The other ParsedElements are supporting ones,
   * e.g. ClassStatements for inner classes
   */
  public static void maybeAnalyze( IParsedElement pe, IParsedElement... other )
  {
    if( !shouldAnalyze() )
    {
      return;
    }

    if( !(pe instanceof IProgram) &&
        (!(pe instanceof IClassFileStatement) ||
         classFileIsNotAnInterface( (IClassFileStatement)pe )) )
    {
      pe.performUnusedElementAnalysis( other );
    }
  }

  private static boolean classFileIsNotAnInterface( IClassFileStatement parsedElementToAnalyze )
  {
    return parsedElementToAnalyze.getClassStatement() != null &&
            !parsedElementToAnalyze.getClassStatement().getGosuClass().isInterface();
  }
}
