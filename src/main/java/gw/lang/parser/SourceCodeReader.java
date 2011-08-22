package gw.lang.parser;

import gw.util.StreamUtil;

import java.io.IOException;
import java.io.Reader;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class SourceCodeReader
{
  private CharSequence _strSource;
  private int _iLength;
  private int _iPosition;

  public SourceCodeReader( CharSequence s )
  {
    this (s, 0);
  }

  public SourceCodeReader( CharSequence s, int offset )
  {
    _strSource = s;
    _iLength = _strSource == null ? -1 : _strSource.length();
    _iPosition = offset;
  }

  public SourceCodeReader( SourceCodeReader otherReader )
  {
    _strSource = otherReader._strSource;
    _iLength = _strSource == null ? -1 : _strSource.length();
    _iPosition = otherReader._iPosition;
  }

  public int read() throws IOException
  {
    return _iLength > _iPosition
           ? _strSource.charAt( _iPosition++ )
           : -1;
  }

  public int peek()
  {
    return _iLength > _iPosition
           ? _strSource.charAt( _iPosition )
           : -1;
  }

  public int getPosition()
  {
    return _iPosition;
  }
  public void setPosition( int iPosition ) throws IOException
  {
    if( iPosition < 0 )
    {
      throw new IOException( iPosition + " < 0" );
    }
    _iPosition = iPosition;
  }

  public String getSource()
  {
    return _strSource.toString();
  }

  public static SourceCodeReader makeSourceCodeReader( Reader reader )
  {
    try
    {
      return new SourceCodeReader( StreamUtil.getContent( reader ) );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }
}
