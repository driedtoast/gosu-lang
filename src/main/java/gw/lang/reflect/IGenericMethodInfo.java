package gw.lang.reflect;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.gs.IGenericTypeVariable;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGenericMethodInfo
{
  /**
   * @return An array of generic type variables if this feature corresponds with
   *         a generic type.
   */
  public IGenericTypeVariable[] getTypeVariables();

  public IType getParameterizedReturnType( IType... typeParams );

  public IType[] getParameterizedParameterTypes( IType... typeParams );

  /**
   * @param argTypes The argument types from a generic method call.
   *
   * @return A map of inferred type parameters based on the argTypes. The map
   *         contains only the types that could be inferred -- the map may be empty.
   *         <p/>
   *         E.g.,
   *         given generic method: <T> T[] toArray( T[] )
   *         and call: list.toArray( new String[list.size()] );
   *         => the toArray() method call should be automatically parameterized with <String>
   *         based on the new String[0].
   */
  public TypeVarToTypeMap inferTypeParametersFromArgumentTypes( IType... argTypes );
}
