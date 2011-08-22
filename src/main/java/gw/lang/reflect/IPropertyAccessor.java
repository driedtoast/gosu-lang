package gw.lang.reflect;

import gw.lang.PublishInGosu;
import gw.lang.Scriptable;
import gw.lang.annotation.ScriptabilityModifier;

/**
 * A property accessor is used to call the get and set methods of the property.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@PublishInGosu
public interface IPropertyAccessor
{
  /**
   * Returns the value for a property given a "this" object
   * @param ctx the "this" pointer.
   * @return the value of the property for the <i>ctx</i> object
   */
  @Scriptable(ScriptabilityModifier.ALL)
  public Object getValue( Object ctx );

  /**
   * Sets the property to the given value for the <i>ctx</i> object
   * @param ctx the "this" pointer
   * @param value the new value
   */
  @Scriptable(ScriptabilityModifier.ALL)
  public void setValue( Object ctx, Object value );

}
