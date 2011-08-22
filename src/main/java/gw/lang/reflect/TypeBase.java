package gw.lang.reflect;

import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.util.concurrent.LazyVar;

import java.io.ObjectStreamException;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class TypeBase implements IType
{
  private static final IType[] EMPTY_INTRINSIC_TYPES_ARRAY = new IType[0];
  private static final IGenericTypeVariable[] EMPTY_TYPE_VARIABLES_ARRAY = new IGenericTypeVariable[0];
  private transient final LazyVar<Set<? extends IType>> _allTypesInHierarchyCache;
  private transient final LazyVar<IType> _arrayTypeCache;
  private final Class<?> _arrayComponentClass;
  private transient boolean _bDiscarded;

  protected TypeBase(Class<?> arrayComponentClass) {
    _arrayComponentClass = arrayComponentClass;
    _allTypesInHierarchyCache = new LazyVar<Set<? extends IType>>(TypeSystem.getGlobalLock()) {
      protected Set<? extends IType> init() {
        return loadAllTypesInHierarchy();
      }
    };
    _arrayTypeCache = new LazyVar<IType>(TypeSystem.getGlobalLock()) {
      protected IType init() {
        return TypeSystem.getOrCreateTypeReference(new DefaultArrayType(TypeSystem.getOrCreateTypeReference(TypeBase.this),
                getArrayComponentClass(), getTypeLoader()));
      }
    };
  }

  protected TypeBase() {
    this(Object.class);
  }

  public IType getEnclosingType()
  {
    return null;
  }

  public IType getGenericType()
  {
    return null;
  }

  public boolean isFinal()
  {
    return false;
  }

  public boolean isInterface()
  {
    return false;
  }

  public boolean isEnum()
  {
    return false;
  }

  public boolean isParameterizedType()
  {
    return false;
  }

  public boolean isGenericType()
  {
    return false;
  }

  public IGenericTypeVariable[] getGenericTypeVariables()
  {
    return EMPTY_TYPE_VARIABLES_ARRAY;
  }

  public IType getParameterizedType( IType... ofType )
  {
    return null;
  }

  public IType[] getTypeParameters()
  {
    return EMPTY_INTRINSIC_TYPES_ARRAY;
  }

  public boolean isArray()
  {
    return false;
  }

  public boolean isPrimitive()
  {
    return false;
  }

  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    return ((Object[]) array)[iIndex];
  }

  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    // Why does IntelliJ warn here?
    //noinspection RedundantCast
    ((Object[]) array)[iIndex] = value;
  }

  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    return ((Object[]) array).length;
  }

  public IType getComponentType()
  {
    return null;
  }

  public boolean isMutable()
  {
    return true;
  }

  public Object readResolve() throws ObjectStreamException
  {
    return this;
  }

  public boolean isValid()
  {
    return true;
  }

  public int getModifiers()
  {
    return Modifier.PUBLIC;
  }

  public boolean isAbstract()
  {
    return Modifier.isAbstract( getModifiers() );
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String toString()
  {
    return getName();
  }

  public Set<? extends IType> getAllTypesInHierarchy() {
    return _allTypesInHierarchyCache.get();
  }

  protected Set<? extends IType> loadAllTypesInHierarchy() {
    Set<IType> allTypes;
    if( isArray() )
    {
      allTypes = getArrayVersionsOfEachType( getComponentType().getAllTypesInHierarchy() );
    }
    else
    {
      allTypes = getAllClassesInClassHierarchyAsIntrinsicTypes( TypeSystem.getOrCreateTypeReference( this ) );
    }
    return allTypes;
  }

  protected static Set<IType> getAllClassesInClassHierarchyAsIntrinsicTypes( IType type )
  {
    HashSet<IType> typeSet = new HashSet<IType>();
    addAllClassesInClassHierarchy( type, typeSet );
    return typeSet;
  }

  private static void addAllClassesInClassHierarchy(IType type, Set<IType> set) {
    if (!set.add(type)) {
      return;
    }

    for (IType iface : type.getInterfaces()) {
      addAllClassesInClassHierarchy(iface, set);
    }

    if (type.getSupertype() != null) {
      addAllClassesInClassHierarchy(type.getSupertype(), set);
    }

    if (type.isParameterizedType()) {
      addAllClassesInClassHierarchy(type.getGenericType(), set);
    }

    if (type.isGenericType() || !type.isParameterizedType()) {
      addAllClassesInClassHierarchy(TypeSystem.getDefaultParameterizedType( type ), set);
    }
  }

  protected Set<IType> getArrayVersionsOfEachType( Set<? extends IType> componentTypes )
  {
    Set<IType> allTypes = new HashSet<IType>( 1 + componentTypes.size() );
    allTypes.add( IJavaType.OBJECT );
    for( IType componentType : componentTypes )
    {
      allTypes.add( componentType.getArrayType() );
    }
    return allTypes;
  }

  public boolean isAssignableFrom(IType type) {
    //noinspection SuspiciousMethodCalls
    return type.getAllTypesInHierarchy().contains( TypeSystem.getOrCreateTypeReference( this ) );
  }

  public IType getArrayType() {
    return _arrayTypeCache.get();
  }

  public Object makeArrayInstance(int iLength) {
    return Array.newInstance(getArrayComponentClass(), iLength);
  }

  protected Class<?> getArrayComponentClass() {
    return _arrayComponentClass;
  }

  public void unloadTypeInfo() {
    _allTypesInHierarchyCache.clear();
    if (_arrayTypeCache.isLoaded()) {
      _arrayTypeCache.get().unloadTypeInfo();
    }
  }

  public boolean isDiscarded()
  {
    return _bDiscarded;
  }

  public void setDiscarded( boolean bDiscarded )
  {
    _bDiscarded = bDiscarded;
  }

  public boolean isCompoundType() {
    return false;
  }

  public Set<IType> getCompoundTypeComponents() {
    return null;
  }

}
