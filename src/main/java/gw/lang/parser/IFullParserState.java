/**
 */
package gw.lang.parser;

/**
 * The state of the parser at a given moment in time.  Useful for error feedback.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IFullParserState extends IParserState
{
  /**
   * @return the symbol table at the moment of creation of the parser state
   */
  public ISymbolTable getSymbolTable();

  /**
   * Set the symbol table at the moment of creation of this IParserState
   */
  public void setSymbolTable( ISymbolTable table );

  /**
   * Collapses the token captured by this parser state, if possible
   */
  void collapseToken();

  /**
   * @return true if parse issues need to retain this state's symbol table (e.g. in an IDE)
   */
  boolean isKeepSymbolTableInIssues();
}
