package gw.lang.parser;

import java.util.Map;

/**
 * A symbol table scope. A scope is basically a map of symbol names to symbols.
 * A scope may also be an activation record.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IScope<K, V extends ISymbol> extends Map<K, V>, Cloneable
{
  /**
   * Enforce a cloneable contract.
   */
  @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException"})
  public Object clone();

  /**
   * Get the activation record context. This can be any object representing the
   * activation record e.g., a function symbol, a rule set context, whatever
   * delimits a call boundary.
   *
   * @return The activation context.
   */
  public IActivationContext getActivationCtx();

  /**
   * visit all symbols in this Scope,
   *
   * @return true if the visitor want to continue visitiong other symbol/scope,
   *         false otherwise.
   */
  public int countSymbols();

  V put( K key, V value );

  /**
   * @return the compile-time csr for this scope if it exists
   */
  int getCSR();

  public void setCSR( int csr );

}
