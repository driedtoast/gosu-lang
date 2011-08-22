package gw.lang.reflect;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IConstructorInfo extends IAttributedFeatureInfo
{
  public IType getType();

  public IParameterInfo[] getParameters();

  public IConstructorHandler getConstructor();

  public List<IExceptionInfo> getExceptions();
}
