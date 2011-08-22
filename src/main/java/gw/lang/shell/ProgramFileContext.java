package gw.lang.shell;

import gw.lang.parser.IFileContext;

import java.io.File;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ProgramFileContext implements IFileContext
{
  File _file;

  public ProgramFileContext( File file )
  {
    _file = file;
  }

  @Override
  public String getContextString()
  {
    return null;
  }

  @Override
  public String getClassNameForFile()
  {
    return makeJavaName( _file.getName() );
  }

  @Override
  public String getFileContext()
  {
    return _file.getName();
  }


  private static String makeJavaName( String name )
  {
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < name.length(); i++ )
    {
      char c = name.charAt( i );
      if( !Character.isJavaIdentifierPart( c ) && c != '.' )
      {
        sb.append( "_" );
      }
      else
      {
        sb.append( c );
      }
    }
    return sb.toString();
  }
}
