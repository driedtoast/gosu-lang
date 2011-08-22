package gw.lang.tidb;

import gw.lang.parser.IParsedElement;

import java.util.HashMap;
import java.util.Map;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface IFeatureInfoRecord extends IRecord<Long>
{
  int OFFSET_NOT_APPLICABLE = -1;
  int LENGTH_NOT_APPLICABLE = -1;
  int LINE_NUMBER_NOT_APPLICABLE = -1;
  int COLUMN_NUMBER_NOT_APPLICABLE = -1;

  Map<Integer, DefUse> VALUE_TO_DEF_USE = new HashMap<Integer, DefUse>();

  public static enum DefUse
  {
    DECL( 1 ),
    WRITE( 2 ),
    READ( 3 );

    private final int _value;

    DefUse( int value )
    {
      _value = value;
      IFeatureInfoRecord.VALUE_TO_DEF_USE.put( value, this );
    }

    public int getValue()
    {
      return _value;
    }
  }


  int getRowNumInDB();

  IFeatureInfoId getFeatureInfoID();

  void setRowNumInDB( int rowNumInDB );

  IFeatureInfoId getOwningFeatureInfoID();

  IFeatureInfoId getEnclosingFeatureInfoID();

  Integer getEnclosingFeatureInfoIDFeatureTypeValue();

  IFeatureInfoId getQualifyingEnclosingFeatureInfoID();

  int getOffsetOfRecord();

  int getLengthOfRecord();

  int getLineNumber();

  int getColumnNumber();

  DefUse getDefUse();

  int getDefUseValue();

  @SuppressWarnings({"RedundantIfStatement"})
  boolean equals( Object o );

  int hashCode();

  String toString();

  Long getID();

  IParsedElement getOriginator();
}
