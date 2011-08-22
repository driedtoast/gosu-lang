package gw.lang.debugger;

import gw.util.Base64Util;
import gw.util.io.ObjectInputStreamWithLoader;
import gw.lang.reflect.TypeSystem;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class DebugCallHandler
{
  private IDebugService _debugService;

  DebugCallHandler()
  {
    _debugService = new StandardDebugDriver();
  }

  public String call( String strServiceClass, String strMethodName, String strParamTypes, String strArgValuesArray )
  {
    try
    {
      if( !IDebugService.class.isAssignableFrom( Class.forName( strServiceClass ) ) )
      {
        System.out.println( "cute little mushrooms" );
        throw new IllegalArgumentException( "The " + GosuDebugSocket.PROTOCOL_NAME + " protocol supports only the " + IDebugService.class.getName() + " service\n" +
                                            strServiceClass + " is not assignable to " + IDebugService.class.getName() );
      }
    }
    catch( ClassNotFoundException e )
    {
      throw new RuntimeException( e );
    }

    try
    {
      Class[] paramTypes = getParamTypes( strParamTypes );
      Method method = _debugService.getClass().getMethod( strMethodName, paramTypes );
      method.setAccessible( true );
      Object[] args = strArgValuesArray == null ? null : (Object[])deserializeFromBase64String( strArgValuesArray );
      Object ret = method.invoke( _debugService, args );
      return serializeAsBase64String( (Serializable)ret );
    }
    catch( InvocationTargetException te )
    {
      Throwable t = te.getTargetException();
      if( t instanceof RuntimeException )
      {
        throw (RuntimeException)t;
      }
      else
      {
        throw new RuntimeException( t );
      }
    }
    catch( Throwable t )
    {
      if( t instanceof RuntimeException )
      {
        throw (RuntimeException)t;
      }
      else
      {
        throw new RuntimeException( t );
      }
    }
  }

  public static HashMap<String, Class> PRIMI_TYPES = new HashMap<String, Class>( 9 );

  static
  {
    PRIMI_TYPES.put( "byte", Byte.TYPE );
    PRIMI_TYPES.put( "char", Character.TYPE );
    PRIMI_TYPES.put( "double", Double.TYPE );
    PRIMI_TYPES.put( "float", Float.TYPE );
    PRIMI_TYPES.put( "int", Integer.TYPE );
    PRIMI_TYPES.put( "long", Long.TYPE );
    PRIMI_TYPES.put( "short", Short.TYPE );
    PRIMI_TYPES.put( "boolean", Boolean.TYPE );
    PRIMI_TYPES.put( "void", Void.TYPE );
  }

  private Class[] getParamTypes( String strParamTypes )
  {
    String[] astrParamTypes = (String[])deserializeFromBase64String( strParamTypes );
    Class[] paramTypes = new Class[astrParamTypes.length];
    try
    {
      for( int i = 0; i < astrParamTypes.length; i++ )
      {
        paramTypes[i] = PRIMI_TYPES.get( astrParamTypes[i] );
        if( paramTypes[i] == null )
        {
          paramTypes[i] = Class.forName( astrParamTypes[i] );
        }
      }
      return paramTypes;
    }
    catch( ClassNotFoundException cnfe )
    {
      throw new RuntimeException( cnfe );
    }
  }

  public static String serializeAsBase64String(Serializable source) {
    if (source == null) {
      return null;
    }

    byte[] ser = getBytes(source);
    return Base64Util.encode(ser);
  }

  public static Serializable deserializeFromBase64String( String strBase64Encoding )
  {
    if( strBase64Encoding == null )
    {
      return null;
    }

    try
    {
      byte[] ser = Base64Util.decode( strBase64Encoding );
      ObjectInputStream objIn = new ObjectInputStreamWithLoader( new ByteArrayInputStream( ser ), TypeSystem.getCurrentModule().getClassLoader() );
      return (Serializable)objIn.readObject();
    }
    catch( Throwable t )
    {
      throw new RuntimeException( t );
    }
  }

  public static byte[] getBytes(Serializable source) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      ObjectOutputStream objOut = new ObjectOutputStream(out);
      objOut.writeObject(source);
      objOut.flush();

      return out.toByteArray();
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

}
