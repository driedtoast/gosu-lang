package gw.lang.reflect;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IMethodInfo extends IAttributedFeatureInfo
{
  public IParameterInfo[] getParameters();

  public IType getReturnType();

  public IMethodCallHandler getCallHandler();

  public String getReturnDescription();

  public List<IExceptionInfo> getExceptions();

  String getName();
}
