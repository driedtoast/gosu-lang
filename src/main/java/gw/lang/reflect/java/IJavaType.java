package gw.lang.reflect.java;

import gw.lang.IDimension;
import gw.lang.ILooseDimension;
import gw.lang.parser.IHasInnerClass;
import gw.lang.reflect.IEnhanceableType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.util.Pair;

import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IJavaType extends IJavaBackedType, IEnhanceableType, IHasInnerClass
{
  //
  // Primitives
  //
  IJavaType pVOID = (IJavaType)TypeSystem.get( Void.TYPE );

  IJavaType pBOOLEAN = (IJavaType)TypeSystem.get( Boolean.TYPE );

  IJavaType pBYTE = (IJavaType)TypeSystem.get( Byte.TYPE );

  IJavaType pCHAR = (IJavaType)TypeSystem.get( Character.TYPE );

  IJavaType pDOUBLE = (IJavaType)TypeSystem.get( Double.TYPE );

  IJavaType pFLOAT = (IJavaType)TypeSystem.get( Float.TYPE );

  IJavaType pINT = (IJavaType)TypeSystem.get( Integer.TYPE );

  IJavaType pLONG = (IJavaType)TypeSystem.get( Long.TYPE );

  IJavaType pSHORT = (IJavaType)TypeSystem.get( Short.TYPE );

  //
  // High usage types
  //

  IJavaType STRING = (IJavaType)TypeSystem.get( String.class );

  IJavaType NUMBER = (IJavaType)TypeSystem.get( Number.class );

  IJavaType DOUBLE = (IJavaType)TypeSystem.get( Double.class );

  IJavaType BOOLEAN = (IJavaType)TypeSystem.get( Boolean.class );

  IJavaType OBJECT = (IJavaType)TypeSystem.get( Object.class );

  IJavaType DATE = (IJavaType)TypeSystem.get( Date.class );

  IJavaType BYTE = (IJavaType)TypeSystem.get( Byte.class );

  IJavaType FLOAT = (IJavaType)TypeSystem.get( Float.class );

  IJavaType CHARACTER = (IJavaType)TypeSystem.get( Character.class );

  IJavaType CHARSEQUENCE = (IJavaType)TypeSystem.get( CharSequence.class );

  IJavaType STRINGBUILDER = (IJavaType)TypeSystem.get( StringBuilder.class );

  IJavaType INTEGER = (IJavaType)TypeSystem.get( Integer.class );

  IJavaType LONG = (IJavaType)TypeSystem.get( Long.class );

  IJavaType SHORT = (IJavaType)TypeSystem.get( Short.class );

  IJavaType BIGDECIMAL = (IJavaType)TypeSystem.get( BigDecimal.class );

  IJavaType BIGINTEGER = (IJavaType)TypeSystem.get( BigInteger.class );

  IJavaType IDIMENSION = (IJavaType)TypeSystem.get( IDimension.class );

  IJavaType ILOOSEDIMENSION = (IJavaType)TypeSystem.get( ILooseDimension.class );

  IJavaType COLLECTION = (IJavaType)TypeSystem.get( Collection.class );

  IJavaType ITERATOR = (IJavaType)TypeSystem.get( Iterator.class );

  IJavaType COMPARABLE = (IJavaType)TypeSystem.get( Comparable.class );

  IJavaType ITERABLE = (IJavaType)TypeSystem.get( Iterable.class );

  IJavaType LIST = (IJavaType)TypeSystem.get( List.class );

  IJavaType LINKEDLIST = (IJavaType)TypeSystem.get( LinkedList.class );

  IJavaType SET = (IJavaType)TypeSystem.get( Set.class );

  IJavaType PAIR = (IJavaType)TypeSystem.get( Pair.class );

  IJavaType MAP = (IJavaType)TypeSystem.get( Map.class );

  IJavaType HASHSET = (IJavaType)TypeSystem.get( HashSet.class );

  IJavaType ARRAYLIST = (IJavaType)TypeSystem.get( ArrayList.class );

  IJavaType HASHMAP = (IJavaType)TypeSystem.get( HashMap.class );

  IJavaType CLASS = (IJavaType)TypeSystem.get( Class.class );

  IJavaType IINTRINSICTYPE = (IJavaType)TypeSystem.get( IType.class );

  IJavaType THROWABLE = (IJavaType)TypeSystem.get( Throwable.class );

  IJavaType ERROR = (IJavaType) TypeSystem.get( Error.class );

  IJavaType EXCEPTION = (IJavaType) TypeSystem.get( Exception.class );

  IJavaType RUNTIME_EXCEPTION = (IJavaType) TypeSystem.get( RuntimeException.class );

  IJavaType ENUM = (IJavaType) TypeSystem.get(Enum.class);

  IJavaType QNAME = (IJavaType) TypeSystem.get( QName.class );

  /**
   * Returns the java class for this java type
   * @return the java class for this java type
   * @deprecated Use only at runtime.  At compile time use getClassInfo().
   */
  Class getIntrinsicClass();

  IJavaClassInfo getClassInfo();
  /**
   * @return An array of Java types reflecting all the classes and interfaces
   * declared as members of the class represented by this Class object. These
   * include public, protected, internal, and private classes and interfaces
   * declared by the class, but excludes inherited classes and interfaces.
   */
  List<IJavaType> getInnerClasses();

  /**
   * If this is a parameterized type, returns the generic type this type
   * parameterizes. Otherwise, returns null.
   */
  IJavaType getGenericType();

  /**
   * Returns the Gosu proxy for this class.
   */
  IGosuClass getAdapterClass();

  /**
   * Creates the Gosu proxy type for this class.
   * @return The newly created proxy type.
   */
  IGosuClass createAdapterClass();

}
