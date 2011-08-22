package gw.lang.ir.statement;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;

import java.util.List;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR statement representing a case within a switch statement, such as:
 * <code>
 * switch(foo) {
 *   case bar:
 *     . . .
 * }
 * </code>
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRCaseClause {
  private IRExpression _condition;
  private List<IRStatement> _statements;

  public IRCaseClause(IRExpression condition, List<IRStatement> statements) {
    _condition = condition;
    _statements = statements;
  }

  public IRExpression getCondition() {
    return _condition;
  }

  public List<IRStatement> getStatements() {
    return _statements;
  }
}
