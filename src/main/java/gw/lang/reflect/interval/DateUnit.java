package gw.lang.reflect.interval;

import java.util.Calendar;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public enum DateUnit
{
  MILLIS( Calendar.MILLISECOND ),
  SECONDS( Calendar.SECOND ),
  MINUTES( Calendar.MINUTE ),
  HOURS( Calendar.HOUR ),
  DAYS( Calendar.DATE ),
  WEEKS( Calendar.WEEK_OF_YEAR ),
  MONTHS( Calendar.MONTH ),
  YEARS( Calendar.YEAR );

  private int _iCalendarConst;

  DateUnit( int iCalendarConst )
  {
    _iCalendarConst = iCalendarConst;
  }

  public int getCalendarConst()
  {
    return _iCalendarConst;
  }
}
