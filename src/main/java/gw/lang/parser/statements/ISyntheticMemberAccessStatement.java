package gw.lang.parser.statements;

import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.ISynthesizedMemberAccessExpression;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ISyntheticMemberAccessStatement extends IStatement {
  ISynthesizedMemberAccessExpression getMemberAccessExpression();
}
