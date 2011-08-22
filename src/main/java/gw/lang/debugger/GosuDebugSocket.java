package gw.lang.debugger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class GosuDebugSocket extends Thread
{
  public static final String DEBUG_PORT_OPT = "debug_port";
  public static final String DEFAULT_PORT = "7734";
  public static final String PROTOCOL_NAME = "gosu:";
  public static final String NULL_VALUE = "<__NULL__>";
  private static final boolean ENABLE_DEBUG_MESSAGES = true;

  private static GosuDebugSocket INSTANCE;

  private ServerSocket _serverSocket;
  private int _iPort;
  private boolean _bDisposed;
  private DebugCallHandler _callHandler;

  public synchronized static void init()
  {
    if( INSTANCE != null )
    {
      return;
    }

    String strPort = System.getProperty( DEBUG_PORT_OPT );
    if( strPort == null )
    {
      // Must specify -debug_port option on cmd line
      return;
    }
    int iPort = Integer.parseInt( strPort );
    INSTANCE = new GosuDebugSocket( iPort );
    INSTANCE.start();
  }

  public static GosuDebugSocket instance()
  {
    if( INSTANCE == null )
    {
      throw new IllegalStateException( "GosuDebugSocket has not been initialized.\n" +
                                       "If the Gosu runtime is in debug mode, GosuDebugSocket.init()\n" +
                                       "should be called very early during the bootstrap of the runtime." );
    }
    return INSTANCE;
  }

  private GosuDebugSocket( int iPort )
  {
    super( "Gosu Debug Socket" );
    _iPort = iPort;
    setDaemon(true);
  }

  public int getPort()
  {
    return _iPort;
  }

  public synchronized void dispose()
  {
    try
    {
      if( !_bDisposed )
      {
        _bDisposed = true;
        _serverSocket.close();
      }
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  public void run()
  {
    try
    {
      _serverSocket = new ServerSocket( _iPort );
      _callHandler = new DebugCallHandler();
      while( !_serverSocket.isClosed() )
      {
        try
        {
          Socket requestSocket = _serverSocket.accept();
          debugMsg( "REQUEST SOCKET CREATED: " + requestSocket );
          new DebugRequest( requestSocket ).start();
        }
        catch( BindException be )
        {
          if( !_serverSocket.isClosed() )
          {
            throw new RuntimeException( "Gosu debug port: " + _iPort + " is already in use", be );
          }
        }
      }
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  private void debugMsg( String strMsg )
  {
    if( ENABLE_DEBUG_MESSAGES )
    {
      System.out.println( "DEBUG: " + strMsg );
    }
  }

  class DebugRequest extends Thread
  {
    private Socket _requestSocket;

    DebugRequest( Socket requestSocket )
    {
      super( "Gosu Debug Request" );
      _requestSocket = requestSocket;
    }

    public void run()
    {
      try
      {
        DataInputStream in = new DataInputStream( new BufferedInputStream( _requestSocket.getInputStream() ) );
        DataOutputStream out = new DataOutputStream( new BufferedOutputStream( _requestSocket.getOutputStream() ) );

        try
        {
          while( !_requestSocket.isClosed() )
          {
            String strService = in.readUTF();
            String strMethod = in.readUTF();
            String strParameterTypes = in.readUTF();
            String strArgs = in.readUTF();
            debugMsg( "REQUEST SOCKET HANDLING: " + strMethod + " : " + _requestSocket );
            String strValue = _callHandler.call( strService, strMethod, strParameterTypes, strArgs );
            if( strValue == null )
            {
              strValue = NULL_VALUE;
            }
            out.writeUTF( strValue );
            out.flush();
            debugMsg( "REQUEST SOCKET HANDLED: " + strMethod + " : " + _requestSocket );
          }
        }
        finally
        {
          debugMsg( "REQUEST SOCKET CLOSED: " + _requestSocket );
          in.close();
          out.close();
          _requestSocket.close();
        }
      }
      catch( EOFException ex )
      {
        // ignore
      }
      catch( Throwable e )
      {
        throw new RuntimeException( e );
      }
    }
  }
}
