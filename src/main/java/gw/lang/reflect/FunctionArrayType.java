package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class FunctionArrayType extends DefaultNonLoadableArrayType
{
  public FunctionArrayType( IType componentType, Class componentConcreteClass, ITypeLoader typeLoader )
  {
    super( componentType, componentConcreteClass, typeLoader );
  }
}
