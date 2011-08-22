package gw.lang.reflect;

import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.IScriptPartId;


/**
 * IFunctionType defines an interface for encapsulating function type information.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IFunctionType extends IInvocableType
{
  public IType getReturnType();

  public IType[] getParameterTypes();

  /**
   * An associated IMethodInfo. Optional.
   */
  public IMethodInfo getMethodInfo();

  /**
   * Formatted signature of the form "<function> ( param-list )"
   */
  public CaseInsensitiveCharSequence getParamSignature();

  public IFunctionType inferParameterizedTypeFromArgTypesAndContextType(IType[] eArgs, IType ctxType);

  boolean areParamsCompatible( IFunctionType rhsFunctionType );

  IScriptPartId getScriptPart();

  /**
   * @return a new copy of this IFunctionType with the given parameter and return types
   */
  IType newInstance( IType[] paramTypes, IType returnType );
}
