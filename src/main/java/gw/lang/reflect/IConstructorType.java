package gw.lang.reflect;

/**
 * IConstructorType defines an interface for encapsulating contructor type information.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IConstructorType extends IInvocableType
{
  /**
   * Returns the type being contructed.
   */
  public IType getDeclaringType();

  /**
   * The constructor's argument types.  Can be null.
   */
  public IType[] getParameterTypes();

  /**
   * An associated Constructor. Optional.
   */
  public IConstructorInfo getConstructor();

  public String getArgSignature();
}
