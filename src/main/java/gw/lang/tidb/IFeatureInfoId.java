package gw.lang.tidb;

import gw.lang.parser.CaseInsensitiveCharSequence;

import java.util.Map;
import java.util.HashMap;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface IFeatureInfoId extends IRecord<Integer>
{
  Map<Integer, FeatureType> VALUE_TO_TYPE = new HashMap<Integer, FeatureType>();

  void setID( Integer id );

  FeatureType getFeatureType();

  int getFeatureTypeValue();

  CaseInsensitiveCharSequence getFeatureName();

  String getRelativeFeatureName();

  public static enum FeatureType
  {
    TYPEREF( 1 ),
    PROPERTY( 2 ),
    METHOD( 3 ),
    CTOR( 4 ),
    // locally defined symbol
    LOCAL( 5 ),;

    private final int _value;

    FeatureType( int value )
    {
      _value = value;
      VALUE_TO_TYPE.put( value, this );
    }

    public int getValue()
    {
      return _value;
    }

    public static FeatureType get(int value) {
      return VALUE_TO_TYPE.get(value);
    }
  }
}
