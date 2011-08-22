package gw.lang.function;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@SuppressWarnings({"UnusedDeclaration"})
public abstract class Function0 extends AbstractBlock implements IFunction0 {

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 0) {
      throw new IllegalArgumentException("You must pass 0 args to this block, but you passed" + args.length);
    } else { 
      return invoke();
    }
  }

}
