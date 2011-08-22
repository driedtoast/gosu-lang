package gw.lang.parser;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IStackProvider
{
  /**
   * For runtime use. Returns the a thread-safe stack.
   */
  public IStack getStack();

  /**
   * For compile-time assignment of stack indexes.
   */
  public int getNextStackIndex();

  /**
   * For compile-time assignment of stack indexes at a particular scope.
   */
  public int getNextStackIndexForScope( IScope scope );

  /**
   * For compile-time use. Returns true iff an isolated scope is visible.
   */
  public boolean hasIsolatedScope();
}
