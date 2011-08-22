package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IExceptionInfo extends IFeatureInfo
{
  /**
   * Returns the intrinsic type this exception represents
   */
  public IType getExceptionType();
}
