package gw.lang.tidb;

import java.lang.reflect.Method;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public class TypeInfoDatabaseH2
{
  public static ITypeInfoDatabase createAndStartDatabase( String h2Location )
  {
    try
    {
      Class<?> tidbClass = Class.forName( "gw.internal.gosu.tidb.TypeInfoDatabaseImplH2" );
      Method method = tidbClass.getMethod( "createAndStartDatabase", String.class );
      return (ITypeInfoDatabase)method.invoke( null, h2Location );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }
}
