package gw.lang.reflect;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IEnumType extends IType
{
  public List<IEnumValue> getEnumValues();

  public IEnumValue getEnumValue( String strName );
}
