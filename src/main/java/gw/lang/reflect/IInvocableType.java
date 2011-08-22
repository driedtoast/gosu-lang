package gw.lang.reflect;

import gw.lang.parser.CaseInsensitiveCharSequence;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IInvocableType extends IType, INonLoadableType
{
  public IType[] getParameterTypes();

  public String[] getParameterNames();

  public Object[] getDefaultValues();

  public boolean hasOptionalParams();

  public CaseInsensitiveCharSequence getParamSignature();
}
