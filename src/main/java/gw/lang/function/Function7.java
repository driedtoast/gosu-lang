package gw.lang.function;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@SuppressWarnings({"UnusedDeclaration"})
public abstract class Function7 extends AbstractBlock implements IFunction7 { 

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 7) {
      throw new IllegalArgumentException("You must pass 7 args to this block, but you passed" + args.length);
    } else { 
      return invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
    }
  }

}
