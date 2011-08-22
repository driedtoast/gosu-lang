package gw.lang.ir.builder;

import gw.lang.UnstableAPI;
import gw.lang.reflect.Modifier;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRFeatureBuilder<T extends IRFeatureBuilder> extends IRBuilder {
  protected int _modifiers = 0;

  @SuppressWarnings({"unchecked"})
  public T withModifiers(int modifiers) {
    _modifiers = modifiers;
    return (T) this;
  }

  @SuppressWarnings({"unchecked"})
  public T _public() {
    _modifiers = Modifier.setPublic(_modifiers, true);
    return (T) this;
  }

  @SuppressWarnings({"unchecked"})
  public T _private() {
    _modifiers = Modifier.setPrivate(_modifiers, true);
    return (T) this;
  }

  @SuppressWarnings({"unchecked"})
  public T _internal() {
    _modifiers = Modifier.setInternal(_modifiers, true);
    return (T) this;
  }

  @SuppressWarnings({"unchecked"})
  public T _protected() {
    _modifiers = Modifier.setProtected(_modifiers, true);
    return (T) this;
  }

  @SuppressWarnings({"unchecked"})
  public T _static() {
    _modifiers = Modifier.setStatic(_modifiers, true);
    return (T) this;
  }
}