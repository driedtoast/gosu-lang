package gw.lang.shell;

import gw.lang.GosuShop;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.Keyword;
import gw.util.StreamUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
class ClassPreParser
{
  private ISourceCodeTokenizer _tokenizer;
  private File _classFile;
  private String _strPackage;
  private File _parentOfRootPackage;

  private ClassPreParser( String strContent )
  {
    _tokenizer = GosuShop.createSourceCodeTokenizer( strContent );
  }

  static ClassPreParser parse( File file )
  {
    if( file == null )
    {
      return null;
    }
    String strContent;
    try
    {
      strContent = StreamUtil.getContent( StreamUtil.getInputStreamReader( new FileInputStream( file ) ) );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
    if( strContent == null || strContent.length() == 0 )
    {
      return null;
    }

    ClassPreParser parser = new ClassPreParser( strContent );
    parser.initTokenizer();
    parser.parsePackage();
    parser.findParentOfRootPackage( file );
    return parser;
  }

  private void findParentOfRootPackage( File file )
  {
    if( _strPackage == null )
    {
      return;
    }

    _classFile = file.getAbsoluteFile();
    File parent = _classFile.getParentFile();
    File saveParent = parent;
    String[] packages = _strPackage.split( "\\." );
    for( int i = packages.length-1; i >= 0; i-- )
    {
      String strPackage = packages[i];
      if( !parent.getName().equalsIgnoreCase( strPackage ) )
      {
        System.err.println( "Package: " + strPackage + " does not match with directory: " + parent.getAbsolutePath() + " in class: " + file.getAbsolutePath() );
        break;
      }
      parent = parent.getParentFile();
    }
    _parentOfRootPackage = parent == null ? saveParent : parent;
  }

  private void initTokenizer()
  {
    _tokenizer.swallowPragmaIfNecessary();
    try
    {
      _tokenizer.nextToken();
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  private void parsePackage()
  {
    if( match( Keyword.KW_package ) )
    {
      _strPackage = parseDotPathWord();
    }
  }

  String parseDotPathWord()
  {
    StringBuilder sb = new StringBuilder( _tokenizer.getStringValue() );
    if( match( null, ISourceCodeTokenizer.TT_WORD, false ) )
    {
      while( match( null, '.', false ) )
      {
        sb.append( '.' );
        sb.append( _tokenizer.getStringValue() );
        match( null, ISourceCodeTokenizer.TT_WORD, false );
      }
    }
    return sb.toString();
  }

  private boolean match( Keyword token )
  {
    return match( token, false );
  }
  private boolean match( Keyword token, boolean bPeek )
  {
    boolean bMatch = false;

    if( ISourceCodeTokenizer.TT_KEYWORD == _tokenizer.getType() )
    {
      bMatch = token.toString().equalsIgnoreCase( _tokenizer.getStringValue() );
    }

    if( bMatch && !bPeek )
    {
      try
      {
        _tokenizer.nextToken();
      }
      catch( IOException e )
      {
        // ignore
      }
    }

    return bMatch;
  }

  private boolean match( String token, int iType, boolean bPeek )
  {
    boolean bMatch = false;

    if( token != null )
    {
      if( (iType == _tokenizer.getType()) || ((iType == 0) && (_tokenizer.getType() == ISourceCodeTokenizer.TT_WORD)) )
      {
        bMatch = token.equalsIgnoreCase( _tokenizer.getStringValue() );
      }
    }
    else
    {
      bMatch = (_tokenizer.getType() == iType) || isValueKeyword( iType );
    }

    if( bMatch && !bPeek )
    {
      try
      {
        _tokenizer.nextToken();
      }
      catch( IOException e )
      {
        // ignore
      }
    }

    return bMatch;
  }

  private boolean isValueKeyword( int iType )
  {
    return iType == ISourceCodeTokenizer.TT_WORD &&
           _tokenizer.getType() == ISourceCodeTokenizer.TT_KEYWORD &&
           Keyword.isReservedValue( _tokenizer.getStringValue() );
  }

  public File getParentOfRootPackage()
  {
    return _parentOfRootPackage;
  }

  public String getClassName()
  {
    return _strPackage + '.' + getBaseFileName();
  }

  private String getBaseFileName()
  {
    return _classFile.getName().substring( 0, _classFile.getName().indexOf( '.' ) );
  }
}
