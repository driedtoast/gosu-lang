package gw.lang.reflect;

import gw.lang.UnstableAPI;

import java.io.Serializable;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IScriptabilityModifier extends Serializable
{
  public IVisibilityModifierType getType();

  public IScriptabilityModifier[] getQualifiers();

  public boolean hasConstraintQualifiers();

  public boolean hasModifierWithType( IVisibilityModifierType type );

  public IScriptabilityModifier getModifierWithType( IVisibilityModifierType type );

  public boolean satisfiesConstraint( IScriptabilityModifier constraint );
}
