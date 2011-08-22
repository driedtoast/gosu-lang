package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a no-op.  This is only used in very rare cases. 
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRNoOpExpression extends IRExpression {
  @Override
  public IRType getType() {
    return IRTypeConstants.pVOID;
  }
}
