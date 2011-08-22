package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class DefaultNonLoadableArrayType extends DefaultArrayType implements INonLoadableType
{
  public DefaultNonLoadableArrayType( IType componentType, Class componentConcreteClass, ITypeLoader typeLoader )
  {
    super( componentType, componentConcreteClass, typeLoader );
  }

  @Override
  protected IDefaultArrayType makeArrayType()
  {
    return new DefaultNonLoadableArrayType( this, getConcreteClass(), getTypeLoader() );
  }
}
