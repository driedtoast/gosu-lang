package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;

import java.util.List;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing a try/catch/finally statement, such as
 * <code>
 * try {
 *   . . .
 * } catch (Exception e) {
 *   . . .
 * } finally {
 *   . . .
 * }
 *
 * Both catch statements and the finally statement body are optional, but there must be either at least one catch statement
 * or a finally body specified.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRTryCatchFinallyStatement extends IRStatement {
  private IRStatement _tryBody;
  private List<IRCatchClause> _catchStatements;
  private IRStatement _finallyBody;

  public IRTryCatchFinallyStatement(IRStatement tryBody, List<IRCatchClause> catchStatements, IRStatement finallyBody) {
    _tryBody = tryBody;
    _catchStatements = catchStatements;
    _finallyBody = finallyBody;

    tryBody.setParent( this );
    for (IRCatchClause catchClause : catchStatements) {
      catchClause.getBody().setParent( this );
    }
    if (finallyBody != null) {
      finallyBody.setParent( this );
    }
  }

  public IRStatement getTryBody() {
    return _tryBody;
  }

  public List<IRCatchClause> getCatchStatements() {
    return _catchStatements;
  }

  public IRStatement getFinallyBody() {
    return _finallyBody;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement()
  {

    IRTerminalStatement tryStmtTerminal = _tryBody.getLeastSignificantTerminalStatement();
    if( tryStmtTerminal != null )
    {
      if( _catchStatements.isEmpty())
      {
        return tryStmtTerminal;
      }
      IRTerminalStatement catchStmtTerminal = _catchStatements.get( _catchStatements.size() - 1 ).getBody().getLeastSignificantTerminalStatement();
      if( catchStmtTerminal != null )
      {
        return catchStmtTerminal;
      }
    }
    return null;
  }
}
