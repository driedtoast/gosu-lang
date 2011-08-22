package gw.lang.parser;

import gw.lang.parser.expressions.IBlockExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;

/**
 * This is the interface that blocks internally implement in Gosu.  It should *NOT* be
 * extended by non-block classes.  Much hilarity would ensue.
 * 
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IBlockClass extends IGosuClass
{
  public static final String INVOKE_METHOD_NAME = "invoke";
  public static final String INVOKE_WITH_ARGS_METHOD_NAME = "invokeWithArgs";

  public IBlockExpression getBlock();

  IType getBlockType();
}
