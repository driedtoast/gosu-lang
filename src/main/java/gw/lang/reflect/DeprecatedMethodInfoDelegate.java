package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class DeprecatedMethodInfoDelegate extends MethodInfoDelegate
{
  public DeprecatedMethodInfoDelegate( IFeatureInfo container, IMethodInfo source )
  {
    super( container, source );
  }

  @Override
  public boolean isDeprecated()
  {
    return true;
  }
}