package gw.util;

import java.io.InputStream;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
class ProcessGobbler extends Thread
{
  private static final String CONSOLE_NEWLINE = "\n";

  private InputStream _streamToGobble;
  private Writer _buffer;

  public ProcessGobbler( InputStream streamToGobble, Writer buffer )
  {
    _streamToGobble = streamToGobble;
    _buffer = buffer;
  }

  @Override
  public void run()
  {
    try
    {
      BufferedReader br = new BufferedReader( StreamUtil.getInputStreamReader( _streamToGobble ) );
      String line;
      while( (line = br.readLine()) != null )
      {
        _buffer.append( line ).append( CONSOLE_NEWLINE );
      }
    } catch (IOException ioe) {
      //ignore
    }
  }
}
