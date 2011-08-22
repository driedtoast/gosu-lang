package gw.lang.reflect;

import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.java.IJavaType;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class DefaultArrayType implements IDefaultArrayType
{
  private IType _componentType;

  transient private ITypeLoader _typeLoader;
  transient private ITypeInfo _typeInfo;
  transient private Set _allTypesInHierarchy;
  transient private IDefaultArrayType _arrayIntrinsicType;
  transient private Class _concreteClass;
  transient private boolean _bDiscarded;
  private String _name;


  public DefaultArrayType( IType componentType, Class componentConcreteClass, ITypeLoader typeLoader )
  {
    _componentType = componentType;
    _typeLoader = typeLoader;
    _concreteClass = componentConcreteClass == null ? null : Array.newInstance( componentConcreteClass, 0 ).getClass();
    _name = _componentType.getName() + "[]";
    assert (componentType instanceof INonLoadableType) == (this instanceof INonLoadableType) :
      "The loadability of component type of a DefaultArrayType must agree with the loadability of this type.  \n" +
      "If " + componentType.getClass().getName() + " is non loadable, its type must use DefaultNonLoadableArrayType as its array type.";
  }

  public final IType getComponentType()
  {
    return _componentType;
  }

  /**
   */
  public final String getRelativeName()
  {
    return _componentType.getRelativeName() + "[]";
  }

  /**
   */
  public final boolean isArray()
  {
    return true;
  }

  /**
   */
  public final Object readResolve()
  {
    return TypeSystem.getByFullName( getName() );
  }

  public final boolean isValid()
  {
    return true;
  }

  public int getModifiers()
  {
    return _componentType.getModifiers();
  }

  public final boolean isAbstract()
  {
    return false;
  }

  /**
   */
  public final String getName()
  {
    return _name;
  }

  public final String getDisplayName()
  {
    return getName();
  }

  /**
   */
  public final String getNamespace()
  {
    return _componentType.getNamespace();
  }

  public final ITypeLoader getTypeLoader()
  {
    return _typeLoader;
  }

  public final IType getSupertype()
  {
    return null;
  }

  public final IType getEnclosingType()
  {
    return null;
  }

  public final IType getGenericType()
  {
    return null;
  }

  public final boolean isFinal()
  {
    return false;
  }

  public final boolean isInterface()
  {
    return false;
  }

  public boolean isEnum()
  {
    return false;
  }

  public final List<? extends IType> getInterfaces()
  {
    return EMPTY_TYPE_LIST;
  }

  /**
   */
  public final boolean isParameterizedType()
  {
    return false;
  }

  /**
   */
  public final boolean isGenericType()
  {
    return false;
  }

  /**
   */
  public final IGenericTypeVariable[] getGenericTypeVariables()
  {
    return null;
  }

  /**
   */
  public final IType[] getTypeParameters()
  {
    return null;
  }

  /**
   */
  public final IType getParameterizedType( IType... ofType )
  {
    throw new UnsupportedOperationException( getName() + " does not support parameterization." );
  }

  @SuppressWarnings({"unchecked"})
  public final Set getAllTypesInHierarchy()
  {
    if( _allTypesInHierarchy == null )
    {
      Set allTypes = new HashSet();
      allTypes.add( IJavaType.OBJECT );
      for( IType type : _componentType.getAllTypesInHierarchy() )
      {
        allTypes.add( type.getArrayType() );
      }
      if( _concreteClass != null )
      {
        Set<? extends IType> allTypesInHierarchy = TypeSystem.get( _concreteClass.getComponentType() ).getAllTypesInHierarchy();
        for( IType type : allTypesInHierarchy )
        {
          if( type instanceof IJavaType )
          {
            allTypes.add( type.getArrayType() );
          }
        }
      }
      _allTypesInHierarchy = allTypes;
    }

    return _allTypesInHierarchy;
  }

  public final boolean isPrimitive()
  {
    return false;
  }

  public final IDefaultArrayType getArrayType()
  {
    if( _arrayIntrinsicType == null )
    {
      // Check, then synch if necessary, then check again to avoid a race
      synchronized( this )
      {
        if( _arrayIntrinsicType == null )
        {
          // Note: we do the first assignment, then the second, so that the assignment to the instance variable
          // doesn't happen prior to the constructor completing, as the initial assignment might happen
          // before the constructor is done
          @SuppressWarnings({"UnnecessaryLocalVariable"}) IDefaultArrayType
          arrayIntrinsicType = makeArrayType();
          _arrayIntrinsicType = arrayIntrinsicType;
        }
      }
    }
    return _arrayIntrinsicType;
  }

  /**
   * Allows subclasses to return a more specific interfaces
   */
  protected IDefaultArrayType makeArrayType()
  {
    return new DefaultArrayType( getThisRef(), getConcreteClass(), _typeLoader ).getThisRef();
  }

  public Object makeArrayInstance( int iLength )
  {
    return Array.newInstance( getConcreteClass(), iLength );
  }

  public Class getConcreteClass()
  {
    return _concreteClass;
  }

  /**
   */
  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    return Array.get( array, iIndex );
  }

  /**
   */
  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    Array.set( array, iIndex, value );
  }

  /**
   */
  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    return Array.getLength( array );
  }

  /**
   */
  public boolean isAssignableFrom( IType type )
  {
    return type.getAllTypesInHierarchy().contains( getThisRef() ) ||
           (type.isArray() &&
            type.getClass().isInstance( getThisRef() ) &&
            this.getComponentType().isAssignableFrom( type.getComponentType() ));

  }

  public boolean isMutable()
  {
    return true;
  }

  public final ITypeInfo getTypeInfo()
  {
    if( _typeInfo == null )
    {
      _typeInfo = new DefaultArrayTypeInfo( getThisRef() );
    }

    return _typeInfo;
  }

  /**
   */
  public final void unloadTypeInfo()
  {
    _typeInfo = null;
    if( _arrayIntrinsicType != null )
    {
      _arrayIntrinsicType.unloadTypeInfo();
    }
  }

  public String toString()
  {
    return getComponentType().toString() + "[]";
  }

  public IDefaultArrayType getThisRef()
  {
    IType compType = this;
    do
    {
      compType = compType.getComponentType();
    } while( compType.isArray() );

    return compType instanceof INonLoadableType || compType instanceof IJavaType
           ? this
           : (IDefaultArrayType)TypeSystem.getOrCreateTypeReference( this );
  }

  public boolean isDiscarded()
  {
    return _bDiscarded;
  }

  public void setDiscarded( boolean bDiscarded )
  {
    _bDiscarded = bDiscarded;
  }

  public boolean isCompoundType()
  {
    return false;
  }

  public Set<IType> getCompoundTypeComponents()
  {
    return null;
  }

  public IType getConcreteComponentType()
  {
    if( _concreteClass == null )
    {
      return null;
    }
    else
    {
      return TypeSystem.get( _concreteClass );
    }
  }

  @Override
  public Class getBackingClass()
  {
    return getConcreteClass();
  }
}