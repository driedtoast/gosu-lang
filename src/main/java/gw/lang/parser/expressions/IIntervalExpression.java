package gw.lang.parser.expressions;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IIntervalExpression extends IBinaryExpression
{
  boolean isLeftClosed();
  boolean isRightClosed();
}