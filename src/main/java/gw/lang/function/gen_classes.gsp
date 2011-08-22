/*
 * Used to generate the block classes.  Should be run from this directory
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
uses java.lang.StringBuilder

for( arity in 16 + 1 ) {

  var clazz = new StringBuilder()
  var iface = new StringBuilder()

  // class per arity
  clazz.append( "package gw.lang.function;\n" )
       .append( "\n" )
       .append( "public abstract class Function${arity} extends AbstractBlock implements IFunction${arity} { \n" )
       .append( "\n" )
       .append( "  public Object invokeWithArgs(Object[] args) {\n" )
       .append( "    if(args.length != ${arity}) {\n" )
       .append( "      throw new IllegalArgumentException(\"You must pass ${arity} args to this block, but you passed\" + args.length);\n" )
       .append( "    } else { \n" )
       .append( "      return invoke(" )
       
  for( arg in arity index i ) {
    if( i != 0 ) {
      clazz.append( ", " )
    }
    clazz.append( "args[${arg}]" )
  }
  clazz.append( ");\n");

  clazz.append( "    }\n" )
       .append( "  }\n" )
       .append( "\n" )
       .append( "}\n" )

  // interface per arity
  iface.append( "package gw.lang.function;\n" )
       .append( "\n" )
       .append( "public interface IFunction${arity} extends IBlock { \n" )
       .append( "\n" )
       .append( "  public Object invoke(" )
  for( arg in arity index i ) {
    if( i != 0 ) {
      iface.append( ", " )
    }
    iface.append( "Object arg${arg}" )
  }
  iface.append( ");\n")
       .append( "\n" )
       .append( "}\n" )

  new java.io.File( "Function${arity}.java" ). write( clazz.toString() )
  new java.io.File( "IFunction${arity}.java" ). write( iface.toString() )
}