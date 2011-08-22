package gw.lang.parser;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IHasArguments
{
  public int getArgPosition();

  public IExpression[] getArgs();
}
