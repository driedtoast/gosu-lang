package gw.lang.ir;

import gw.lang.reflect.IType;
import gw.lang.UnstableAPI;

import java.lang.reflect.Array;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class SyntheticIRArrayType implements IRType {

  private IRType _componentType;

  public SyntheticIRArrayType(IRType componentType) {
    _componentType = componentType;
  }

  @Override
  public String getName() {
    return _componentType.getName() + "[]";
  }

  @Override
  public String getRelativeName() {
    return _componentType.getRelativeName() + "[]";
  }

  @Override
  public String getDescriptor() {
    return '[' + getComponentType().getDescriptor();
  }

  @Override
  public Class getJavaClass() {
    return Array.newInstance(getComponentType().getJavaClass(), 0).getClass();    
  }

  @Override
  public String getSlashName() {
    return getComponentType().getSlashName() + "[]";
  }

  @Override
  public IRType getArrayType() {
    return new SyntheticIRArrayType(this);
  }

  @Override
  public IRType getComponentType() {
    return null;
  }

  @Override
  public boolean isArray() {
    return true;
  }

  @Override
  public boolean isAssignableFrom(IRType otherType) {
    return otherType.isArray() && getComponentType().isAssignableFrom(otherType.getComponentType());
  }

  @Override
  public boolean isByte() {
    return false;
  }

  @Override
  public boolean isBoolean() {
    return false;
  }

  @Override
  public boolean isShort() {
    return false;
  }

  @Override
  public boolean isChar() {
    return false;
  }

  @Override
  public boolean isInt() {
    return false;
  }

  @Override
  public boolean isLong() {
    return false;
  }

  @Override
  public boolean isFloat() {
    return false;
  }

  @Override
  public boolean isDouble() {
    return false;
  }

  @Override
  public boolean isVoid() {
    return false;
  }

  @Override
  public boolean isPrimitive() {
    return false;
  }

  @Override
  public boolean isInterface() {
    return false;
  }

  @Override
  public IType getType() {
    return null;
  }
}