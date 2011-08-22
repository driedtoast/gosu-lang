package gw.lang.function;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@SuppressWarnings({"UnusedDeclaration"})
public abstract class Function9 extends AbstractBlock implements IFunction9 { 

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 9) {
      throw new IllegalArgumentException("You must pass 9 args to this block, but you passed" + args.length);
    } else { 
      return invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]);
    }
  }

}
