package gw.lang.function;

import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.gs.IGosuClassObject;
import gw.lang.parser.expressions.IBlockExpression;

/**
 * All blocks in Gosu implement this interface at runtime.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IBlock extends IGosuClassObject
{
  public static final int MAX_ARGS = 16;

  public Object invokeWithArgs( Object... args );

  public IBlockExpression getParsedElement();

  IFunctionType getFunctionType();
}
