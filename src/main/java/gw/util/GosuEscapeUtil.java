package gw.util;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class GosuEscapeUtil
{
  /**
   * Escape any special characters in the string, using the Java escape syntax.
   * For example any tabs become \t, newlines become \n etc.
   *
   * @return the escaped string. Returns the original string unchanged if it
   *         contains no special characters.
   */
  public static String escapeForJava( String string )
  {
    String result;
    StringBuffer resultBuffer = null;
    for( int i = 0, length = string.length(); i < length; i++ )
    {
      char ch = string.charAt( i );
      String escape = escapeForJava( ch );
      if( escape != null )
      {
        if( resultBuffer == null )
        {
          resultBuffer = new StringBuffer( string );
          resultBuffer.setLength( i );
        }
        resultBuffer.append( escape );
      }
      else if( resultBuffer != null )
      {
        resultBuffer.append( ch );
      }
    }
    result = (resultBuffer != null) ? resultBuffer.toString() : string;
    return result;
  }

  /**
   * Converts an escaped character code into a string literal expressing it, e.g. '\n' becomes "\\n".
   *
   * @param ch Escaped character code.
   *
   * @return The string expression of the character code, null if <code>ch</code> is not an escaped character.
   *         Supports Unicode.
   */
  public static String escapeForJava( char ch )
  {
    String escape = escapeForGosuStringLiteral( ch );
    if( escape == null )
    {
      if( ch <= 31 || ch >= 127 )
      {
        escape = getUnicodeEscape( ch );
      }
    }
    return escape;
  }

  public static String escapeForGosuStringLiteral( String strText )
  {
    StringBuilder sb = new StringBuilder( strText.length() );
    for( int i = 0; i < strText.length(); i++ )
    {
      sb.append( escapeForGosuStringLiteral( strText.charAt( i ) ) );
    }
    return sb.toString();
  }

  public static String escapeForGosuStringLiteral( char ch )
  {
    String escape = Character.toString( ch );
    switch( ch )
    {
      case '\b':
        escape = "\\b";
        break;
      case '\t':
        escape = "\\t";
        break;
      case '\n':
        escape = "\\n";
        break;
      case '\f':
        escape = "\\f";
        break;
      case '\r':
        escape = "\\r";
        break;
      case '\"':
        escape = "\\\"";
        break;
      case '\'':
        escape = "\\'";
        break;
      case '\\':
        escape = "\\\\";
        break;
      default:
        break;
    }
    return escape;
  }

  public static String getUnicodeEscape( char ch )
  {
    String prefix = "\\u";
    int length = prefix.length() + 4;
    String hex = Integer.toHexString( ch );
    StringBuffer resultBuffer = new StringBuffer( length );
    resultBuffer.append( prefix );
    for( int i = 0, n = length - (prefix.length() + hex.length()); i < n; i++ )
    {
      resultBuffer.append( '0' );
    }
    resultBuffer.append( hex );
    return resultBuffer.toString();
  }

  public static String stripNewLinesAndExtraneousWhiteSpace( String s )
  {
    if( s == null )
    {
      return null;
    }

    StringBuffer result = new StringBuffer();
    boolean hitNewLine = false;
    boolean addedSpace = false;
    for( int i = 0; i < s.length(); i++ )
    {
      char c = s.charAt( i );
      if( c == '\n' )
      {
        hitNewLine = true;
      }
      else if( c == ' ' )
      {
        if( hitNewLine )
        {
          if( !addedSpace )
          {
            result.append( c );
            addedSpace = true;
          }
        }
        else
        {
          result.append( c );
        }
      }
      else
      {
        hitNewLine = false;
        addedSpace = false;
        result.append( c );
      }
    }
    return result.toString().trim();
  }

  public static String escapeForHTML( String string )
  {
    return escapeForHTML( string, true );
  }

  public static String escapeForHTML( String string, boolean escapeWhitespace )
  {
    if( string == null || string.length() == 0 )
    {
      return string;
    }
    StringBuilder resultBuffer = null;
    char last = 0;
    for( int i = 0, length = string.length(); i < length; i++ )
    {
      String entity = null;
      char ch = string.charAt( i );
      switch( ch )
      {
        case '<':
        {
          entity = "&lt;";
          break;
        }
        case ' ':
          if( last == ' ' && escapeWhitespace )
          {
            entity = "&nbsp;";
          }
          break;
        case '>':
          entity = "&gt;";
          break;
        case '&':
          entity = "&amp;";
          break;
        case '"':
          entity = "&quot;";
          break;
        case '\n':
          if( escapeWhitespace )
          {
            entity = "<br>";
          }
          break;
        default:
          break;
      }
      if( entity != null )
      {
        if( resultBuffer == null )
        {
          resultBuffer = new StringBuilder( string );
          resultBuffer.setLength( i );
        }
        resultBuffer.append( entity );
      }
      else if( resultBuffer != null )
      {
        resultBuffer.append( ch );
      }
      last = ch;
    }
    return (resultBuffer != null) ? resultBuffer.toString() : string;
  }
}
