package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.reflect.IBlockType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IBlockInvocation extends IExpression
{
  public IBlockType getBlockType();
}