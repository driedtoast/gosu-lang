package gw.util.io;

import java.io.ObjectInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.ObjectStreamClass;
import java.lang.reflect.Array;

/**
 * This class enables deserialization class instances where the class is
 * loaded from a special loader e.g., Gosu classes extending Java classes.<p>
 * <pre>
 *    // This example uses ObjectInputStreamWithLoader along with the Gosu plugin loader
 *    // to enable Gosu -> Java serialization.
 *    ObjectInputStream objIn =
 *      new ObjectInputStreamWithLoader( new ByteArrayInputStream( ser ), 
 *                                       CommonServices.getEntityAccess().getPluginClassLoader() );
 * </pre>
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ObjectInputStreamWithLoader extends ObjectInputStream
{
  private ClassLoader loader;

  /**
   * Loader must be non-null;
   */

  public ObjectInputStreamWithLoader( InputStream in, ClassLoader loader ) throws IOException
  {
    super( in );
    if( loader == null )
    {
      throw new IllegalArgumentException( "Illegal null argument to ObjectInputStreamWithLoader" );
    }
    this.loader = loader;
  }

  /**
   * Make a primitive array class
   */

  private Class primitiveType( char type )
  {
    switch( type )
    {
      case 'B':
        return byte.class;
      case 'C':
        return char.class;
      case 'D':
        return double.class;
      case 'F':
        return float.class;
      case 'I':
        return int.class;
      case 'J':
        return long.class;
      case 'S':
        return short.class;
      case 'Z':
        return boolean.class;
      default:
        return null;
    }
  }

  /**
   * Use the given ClassLoader rather than using the system class
   */
  protected Class resolveClass( ObjectStreamClass classDesc )throws IOException, ClassNotFoundException
  {

    String cname = classDesc.getName();
    if( cname.startsWith( "[" ) )
    {
      // An array
      Class component;    // component class
      int dcount;      // dimension
      for( dcount = 1; cname.charAt( dcount ) == '['; dcount++ )
      {
        ;
      }
      if( cname.charAt( dcount ) == 'L' )
      {
        component = loader.loadClass( cname.substring( dcount + 1,
                                                       cname.length() - 1 ) );
      }
      else
      {
        if( cname.length() != dcount + 1 )
        {
          throw new ClassNotFoundException( cname );// malformed
        }
        component = primitiveType( cname.charAt( dcount ) );
      }
      int dim[] = new int[dcount];
      for( int i = 0; i < dcount; i++ )
      {
        dim[i] = 0;
      }
      return Array.newInstance( component, dim ).getClass();
    }
    else
    {

      return loader.loadClass( cname );
    }
  }
}
