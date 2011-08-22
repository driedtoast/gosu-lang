package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing a list of other statements.  The statement list may or may not create a new scope;
 * in the case of explicitly created statement lists (i.e. by enclosing code within { } ), a scope will be created,
 * while in the case of implicitly created statement lists (i.e. lists of statements created to handle composite
 * statements) a scope will not.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRStatementList extends IRStatement {
  private List<IRStatement> _statements;
  private boolean _hasScope = true;

  public IRStatementList(boolean hasScope, IRStatement... statements) {
    _hasScope = hasScope;
    _statements = new ArrayList<IRStatement>();
    _statements.addAll(Arrays.asList(statements));

    for (IRStatement statement : statements) {
      statement.setParent( this );
    }
  }

  public IRStatementList(boolean hasScope, List<IRStatement> statements) {
    // TODO - AHK - Should I be paranoid and copy this?
    _hasScope = hasScope;
    _statements = statements;
    for (IRStatement statement : statements) {
      if (statement != null) {
        statement.setParent( this );
      }
    }
  }

  public void addStatement(IRStatement statement) {
    _statements.add(statement);
    statement.setParent( this );
  }

  public List<IRStatement> getStatements() {
    return _statements;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement()
  {
    for( int i = 0; i < _statements.size(); i++ )
    {
      if (_statements.get(i) != null) {
        IRTerminalStatement terminalStmt = _statements.get(i).getLeastSignificantTerminalStatement();
        if( terminalStmt != null )
        {
          return terminalStmt;
        }
      }
    }
    return null;
  }
  
  public void mergeStatements( IRStatement irStatement )
  {
    if( irStatement instanceof IRStatementList )
    {
      for( IRStatement statement : ((IRStatementList)irStatement).getStatements() )
      {
        addStatement( statement );
      }
    }
    else
    {
      addStatement( irStatement );
    }
  }

  public boolean hasScope()
  {
    return _hasScope;
  }
}
