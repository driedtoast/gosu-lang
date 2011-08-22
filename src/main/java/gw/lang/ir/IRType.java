package gw.lang.ir;

import gw.lang.reflect.IType;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IRType {

  String getName();

  String getRelativeName();

  String getDescriptor();

  // Note:  This method should ONLY be called AFTER compilation
  Class getJavaClass();

  String getSlashName();

  IRType getArrayType();

  IRType getComponentType();

  boolean isArray();

  boolean isAssignableFrom( IRType otherType );

  boolean isByte();

  boolean isBoolean();

  boolean isShort();

  boolean isChar();

  boolean isInt();

  boolean isLong();

  boolean isFloat();

  boolean isDouble();

  boolean isVoid();

  boolean isPrimitive();

  boolean isInterface();

  IType getType();
}
