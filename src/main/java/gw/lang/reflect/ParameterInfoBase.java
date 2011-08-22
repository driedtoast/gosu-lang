package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ParameterInfoBase implements IParameterInfo
{
  private IMethodInfo _container;
  private String _name;
  private IType _type;


  public ParameterInfoBase( IMethodInfo container, String name, IType type)
  {
    _container = container;
    _name = name;
    _type = type;
  }

  public String getName() {
    return _name;
  }

  public IType getFeatureType() {
    return _type;
  }

  public IFeatureInfo getContainer()
  {
    return _container;
  }

  public IType getOwnersType()
  {
    return _container.getOwnersType();
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getDescription()
  {
    return null;
  }

}
