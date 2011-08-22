package gw.lang.debugger;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class GosuRemoteServiceProtocolHandler implements RemoteServiceProtocolHandler
{
  private int _iPort;
  private DebugSocket _cmdSocket;
  private DebugSocket _apiSocket;

  public GosuRemoteServiceProtocolHandler( int iPort )
  {
    _iPort = iPort;
  }

  public String call( String strSessionId, String strService, String strMethod, String strParamTypes, String strArgs ) throws Exception
  {
    DebugSocket debugSocket;
    if( strMethod.equals( "debug" ) )
    {
      if( _cmdSocket == null )
      {
        _cmdSocket = new DebugSocket( _iPort );
      }
      debugSocket = _cmdSocket;
    }
    else
    {
      if( _apiSocket == null )
      {
        _apiSocket = new DebugSocket( _iPort );
      }
      debugSocket = _apiSocket;
    }

    return debugSocket.call( strService, strMethod, strParamTypes, strArgs );
  }

  public void dispose()
  {
    if( _cmdSocket != null )
    {
      _cmdSocket.dispose();
    }
    if( _apiSocket != null )
    {
      _apiSocket.dispose();
    }
  }

  public class DebugSocket
  {
    private int _iPort;
    private Socket _socket;
    private DataInputStream _in;
    private DataOutputStream _out;

    public DebugSocket( int iPort )
    {
      _iPort = iPort;
    }

    public String call( String strService, String strMethod, String strParamTypes, String strArgs ) throws Exception
    {
      if( _socket == null || _socket.isClosed() || _socket.isInputShutdown() || _socket.isOutputShutdown() )
      {
        _socket = new Socket( "localhost", _iPort );
        _in = new DataInputStream( new BufferedInputStream( _socket.getInputStream() ) );
        _out = new DataOutputStream( new BufferedOutputStream( _socket.getOutputStream() ) );
      }

      _out.writeUTF( strService );
      _out.writeUTF( strMethod );
      _out.writeUTF( strParamTypes );
      _out.writeUTF( strArgs );
      _out.flush();
      String retValue = _in.readUTF();
      if( GosuDebugSocket.NULL_VALUE.equals( retValue ) )
      {
        retValue = null;
      }
      return retValue;
    }

    public void dispose()
    {
      try
      {
        if( !_socket.isClosed() )
        {
          _in.close();
          _out.close();
          _socket.close();
        }
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
  }
}
