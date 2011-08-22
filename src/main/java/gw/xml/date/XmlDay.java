package gw.xml.date;

import java.util.Calendar;
import java.text.ParseException;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class XmlDay extends AbstractXmlDateType
{

  private int _day;

  private XmlDay()
  {
    super( false, false, true, false );
  }

  public XmlDay( String s ) throws ParseException {
    this();
    parseString( s );
  }

  public XmlDay( Calendar cal, boolean useTimeZone )
  {
    this();
    getCalendarFields( cal, useTimeZone );
  }

  public int getDay() {
    return _day;
  }

  public void setDay( int day ) {
    _day = day;
  }

}

