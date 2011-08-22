package gw.lang.function;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class Function1 extends AbstractBlock implements IFunction1 { 

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 1) {
      throw new IllegalArgumentException("You must pass 1 args to this block, but you passed" + args.length);
    } else { 
      return invoke(args[0]);
    }
  }

}
