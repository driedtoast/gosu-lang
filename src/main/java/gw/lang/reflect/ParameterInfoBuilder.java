package gw.lang.reflect;

/**
 * A builder for creating IParameterInfos from scratch.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ParameterInfoBuilder {

  private String _name;
  private String _description;
  private IType _type;
  private Object _defValue;

  public ParameterInfoBuilder withName(String name) {
    this._name = name;
    return this;
  }

  public ParameterInfoBuilder withDescription(String description) {
    this._description = description;
    return this;
  }

  public ParameterInfoBuilder withType(IType type) {
    this._type = type;
    return this;
  }

  public ParameterInfoBuilder withDefValue(Object value) {
    _defValue = value;
    return this;
  }

  public ParameterInfoBuilder withType(Class clazz) {
    this._type = TypeSystem.get(clazz);
    return this;
  }

  public ParameterInfoBuilder like(IParameterInfo param) {
    withName(param.getName());
    withDescription(param.getDescription());
    withType(param.getFeatureType());
    return this;
  }
  
  public IParameterInfo build(IFeatureInfo container) {
    return new BuiltParameterInfo(this, container);
  }

  public static class BuiltParameterInfo implements IParameterInfo {

    private final IFeatureInfo _container;
    private final String _name;
    private final String _description;
    private final IType _type;
    private final Object _defValue;

    public BuiltParameterInfo(ParameterInfoBuilder builder, IFeatureInfo container) {
      assert container != null;
      assert builder._name != null;
      assert builder._type != null;
      this._container = container;
      this._name = builder._name;
      this._description = builder._description;
      this._type = builder._type;
      this._defValue = builder._defValue;
    }

    public IFeatureInfo getContainer() {
      return this._container;
    }

    public IType getOwnersType() {
      return this._container.getOwnersType();
    }

    public String getName() {
      return this._name;
    }

    public String getDisplayName() {
      return this._name;
    }

    public String getDescription() {
      return this._description;
    }

    public IType getFeatureType() {
      return this._type;
    }

    public Object getDefaultValue() {
      return this._defValue;
    }
  }

}
