package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a character literal, i.e. <code>'a'</code>
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class IRCharacterLiteral extends IRExpression
{
  private char _char;

  public IRCharacterLiteral( char c )
  {
    _char = c;
  }

  public char getValue()
  {
    return _char;
  }

  @Override
  public IRType getType()
  {
    return IRTypeConstants.pCHAR;
  }
}