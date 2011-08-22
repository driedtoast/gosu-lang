package gw.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.AbstractList;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class RegExpMatch
{
  private Matcher _matcher;
  private List<String> _groups;

  public RegExpMatch( Matcher matcher )
  {
    _matcher = matcher;
    _groups = new GroupList();
  }

  public String get( int index )
  {
    return _matcher.group( index + 1 );
  }

  public int size()
  {
    return _matcher.groupCount() - 1;
  }

  public Matcher getMatcher()
  {
    return _matcher;
  }

  public Pattern getPattern()
  {
    return _matcher.pattern();
  }

  /**
   * @return a list of matched groups.  Unlike java.util.regex.Matcher,
   * the entire string is not considered an implicit group.
   */
  public List<String> getGroups()
  {
    return _groups;
  }

  private class GroupList extends AbstractList<String> implements List<String> {

    public String get( int index )
    {
      return _matcher.group( index + 1 );
    }

    public int size()
    {
      return _matcher.groupCount();
    }
  }

  @Override
  public String toString() {
    return "[RegExpMatch: " + _groups + "]";
  }
}
