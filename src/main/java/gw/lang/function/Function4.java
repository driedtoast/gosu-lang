package gw.lang.function;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@SuppressWarnings({"UnusedDeclaration"})
public abstract class Function4 extends AbstractBlock implements IFunction4 { 

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 4) {
      throw new IllegalArgumentException("You must pass 4 args to this block, but you passed" + args.length);
    } else { 
      return invoke(args[0], args[1], args[2], args[3]);
    }
  }

}
