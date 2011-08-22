package gw.lang.reflect.gs;


/**
 * The IExternalSymbolMap is used to pass external symbols to Gosu programs and fragments when they're executed.
 * When a program or fragment is compiled, any references to symbols defined as external at compilation time
 * will result in calls to this interface.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IExternalSymbolMap {

  /**
   * Returns the value for the symbol with the given name.  Throws a runtime exception if the name does not
   * correspond to a valid external symbol.
   *
   * @param name the name of the symbol
   * @return the current value of the symbol
   */
  Object getValue(String name);

  /**
   * Sets the value of the symbol with the given name.  Throws a runtime exception if the name does not
   * correspond to a valid external symbol.
   *
   * @param name the name of the symbol
   * @param value the new value to give that symbol
   */
  void setValue(String name, Object value);

  /**
   * Invokes the named external function with the given arguments.  The name argument should correspond to
   * the result of calling getName() on the external function symbol.
   *
   * @param name the name of the function symbol
   * @param args the arguments to the method
   * @return the result of the function invocation
   */
  Object invoke(String name, Object[] args);
}
