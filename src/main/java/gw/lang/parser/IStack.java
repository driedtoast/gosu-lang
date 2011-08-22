package gw.lang.parser;

import java.util.List;
import java.util.Map;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IStack
{
  public static final int THIS_POS = 0;
  public static final int SUPER_POS = 1;
  public static final int START_POS = 2;

  public static final String UNASSIGNED_LABEL = "<unassigned>";

  /**
   * Push a local scope context onto the stack.
   *
   * @return The pushed scope.
   */
  public int pushScope( IParsedElement context );

  /**
   * Pop a local scope context from the stack.
   * <p/>
   * See pushScope() for implementation suggestions.
   *
   * @return The popped scope.
   */
  public int popScope( Object context );

  /**
   * Peek at the scope at the top of the stack.
   */
  public Object peekScope();

  public Object getValue( ISymbol symbol );

  public void setValue( ISymbol symbol, Object value );

  public Object getGlobalValue( ISymbol symbol );

  public void setGlobalValue( ISymbol symbol, Object value );

  public void pushGlobalScope( IParsedElement context );

  public void popGlobalScope( IParsedElement context );

  public IActivationContext[] getIsolatedScopes();

  public IActivationContext getICurrentScopeIndexForDebugger();

  public IActivationContext getNextIsolatedScope( IActivationContext ctx );

  /**
   * Returns a 'this' reference.
   */
  public ISymbol getThisSymbol();

  /**
   * @return The offset from the most resently pushed isolated scope
   */
  public int getCurrentOffset();

  /**
   * @return A list of currently mapped ISymbols e.g., the values in a hash
   *         table based implementation.
   */
  public Map getSymbols();

  /**
   * @param iScopeIndex Scopes positioned on the stack at an index greater than
   *                    this number are not included. Very useful for examining a specific scope
   *                    e.g., for a debugger. Note an index < 0 indicates that all scopes are
   *                    included.
   *
   * @return A list of currently mapped ISymbols e.g., the values in a hash
   *         table based implementation.
   */
  public Map getSymbols( int iScopeIndex, int iPrivateGlobalIndex );

  /**
   * @return The number of scopes on the stack. These include all scopes:
   *         global, isolated, and local. Useful for recording a specific offset in the
   *         symbol table e.g., a debugger needs this to jump to a position in a call
   *         stack.
   *
   * @see #getSymbols(int,int)
   */
  public int getScopeCount();

  /**
   * @return The number of scopes on the private global stack. Useful for
   *         recording a specific offset in the symbol table e.g., a debugger needs
   *         this to jump to a position in a call stack.
   *
   * @see #getSymbols(int,int)
   */
  public int getPrivateGlobalScopeCount();

  /**
   * Push a scope that demarcates an activation record. The behavior is nearly
   * identical to pushScope(), the [big] difference is that activation record
   * scopes cannot access symbols from other activation record scopes.
   * <p/>
   * Use popScope() to pop a scope pushed via this method.
   *
   * @param activationCtx The context for the activation record.
   *
   * @return The index of the scope
   */
  public int pushIsolatedScope( IActivationContext activationCtx );

  /**
   * Push a global scope you specify onto the private global scope space. Useful
   * for handling private global scopes for libraries, namespaces, etc. As this
   * functionality is primarily for Gosu runtime, you'll likely never need to
   * call this.
   * <p/>
   * If you need to push a scope with restricted visibility, consider calling
   * <code>pushIsolatedScope()</code> instead.
   *
   * @see #pushScope(IParsedElement)
   * @see #pushIsolatedScope(IActivationContext)
   */
  public void pushPrivateGlobalScope( IStack stack );

  /**
   * Pops a global scope previously pushed via <code>pushGlobalScope( IScope )</code>
   * or <code>pushPrivateGlobalScope( IScope )</code>.
   * <p/>
   * You probably shouldn't call this method.
   *
   * @see #pushPrivateGlobalScope(IStack)
   */
  public void popGlobalScope( IStack stack );

  /**
   * Get a filo list of <i>isolated</i> scopes currently in this table.
   *
   * @return A list of objects pushed as activation contexts via
   *         pushIsolatedScope().
   */
  public List getCallStack();

  void setCurrentScopeIndexForDebugger(int currentIsolatedScope);
}
