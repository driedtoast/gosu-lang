package gw.lang.ir.builder;

import gw.lang.ir.IRType;
import gw.lang.GosuShop;
import gw.lang.UnstableAPI;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassInfo;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public abstract class IRElementBuilder {

  // ------------------------ Protected Helper Methods

  protected static IRType getIRType( Class cls ) {
    return GosuShop.getIRTypeResolver().getDescriptor( cls );
  }

  protected static IRType getIRType( IType type ) {
    return GosuShop.getIRTypeResolver().getDescriptor( type );
  }

  protected static IRType getIRType( IJavaClassInfo cls ) {
    return GosuShop.getIRTypeResolver().getDescriptor( cls );
  }

  protected static List<IRType> getIRTypes( Class[] classes ) {
    List<IRType> results = new ArrayList<IRType>();
    for (Class cls : classes) {
      results.add( getIRType( cls ) );
    }
    return results;
  }

  // ------------------------ Constructor Resolution

  protected static Constructor findConstructor( Class cls, int numArgs ) {
    Constructor match = null;
    for (Constructor cons : cls.getDeclaredConstructors()) {
      if (cons.getParameterTypes().length == numArgs) {
        if (match != null) {
          throw new IllegalArgumentException("The call to create a new " + cls + " with " + numArgs + " arguments is ambiguous");
        } else {
          match = cons;
        }
      }
    }

    if (match == null) {
      throw new IllegalArgumentException("No constructor with " + numArgs + " arguments found on class " + cls);
    }

    return match;
  }

  // ------------------------ Method Resolution

  protected static Method findMethod( Class cls, String name, int numArgs ) {
    Method match = findUniqueMethodInList( cls.getMethods(), cls, name, numArgs );

    if (match != null) {
      return match;
    }

    match = findDeclaredMethod( cls, name, numArgs );
    if (match != null) {
      return match;
    }

    if ( name.equals("toString") || name.equals("equals") || name.equals("hashCode")) {
      return findMethod( Object.class, name, numArgs );
    }

    throw new IllegalArgumentException("No method named " + name + " with " + numArgs + " arguments found starting from class " + cls);
  }

  private static Method findDeclaredMethod( Class cls, String name, int numArgs ) {
    Method match = findUniqueMethodInList( TypeSystem.getDeclaredMethods( cls ), cls, name, numArgs );

    if (match != null) {
      return match;
    }

    if ( cls.getSuperclass() != null ) {
      return findDeclaredMethod( cls.getSuperclass(), name, numArgs );
    }

    return null;
  }

  private static Method findUniqueMethodInList( Method[] methods, Class cls, String name, int numArgs ) {
    Method match = null;
    for (Method m : methods) {
      if (m.getName().equals(name) && m.getParameterTypes().length == numArgs) {
        if (match != null) {
          throw new IllegalArgumentException("The call to method " + name + " on class " + cls.getName() + " with " + numArgs + " arguments is ambiguous");
        }
        match = m;
      }
    }

    return match;
  }

  // ------------------------- Field Resolution

  protected static Field findField( Class cls, String name ) {
    Field[] allFields = cls.getFields();

    for (Field f : allFields) {
      if (f.getName().equals(name)) {
        return f;
      }
    }

    Field match = findDeclaredField( cls, name );
    if ( match != null ) {
      return match;
    }

    throw new IllegalArgumentException("No field named " + name + " found starting from class " + cls);
  }

  private static Field findDeclaredField( Class cls, String name ) {
    Field[] declaredFields = cls.getDeclaredFields();

    for (Field f : declaredFields) {
      if (f.getName().equals(name)) {
        return f;
      }
    }

    if ( cls.getSuperclass() != null ) {
      return findDeclaredField( cls.getSuperclass(), name );
    }

    return null;
  }

}
