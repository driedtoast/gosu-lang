package gw.lang.shell;

import gw.lang.GosuShop;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.Keyword;
import gw.util.StreamUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
class ProgramDirectiveParser
{
  private ISourceCodeTokenizer _tokenizer;
  private ProgramDirectiveInfo _directiveInfo;

  private ProgramDirectiveParser( String strContent )
  {
    _tokenizer = GosuShop.createSourceCodeTokenizer( strContent );
    _directiveInfo = new ProgramDirectiveInfo();
  }

  static ProgramDirectiveInfo parse( File file )
  {
    try
    {
      if( file == null )
      {
        return null;
      }
      String strContent = StreamUtil.getContent( StreamUtil.getInputStreamReader( new FileInputStream( file ) ) );
      return parse( strContent );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  static ProgramDirectiveInfo parse( String strContent )
  {
    if( strContent == null || strContent.length() == 0 )
    {
      return null;
    }

    ProgramDirectiveParser parser = new ProgramDirectiveParser( strContent );
    parser.initTokenizer();
    parser.parseClasspaths();
    parser.parseTypeLoaders();
    return parser.getDirectiveInfo();
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

  private void parseClasspaths()
  {
    List<String> paths = new ArrayList<String>();
    while( match( Keyword.KW_classpath, false ) )
    {
      String strClasspath = _tokenizer.getStringValue();
      if( !match( null, (int)'"', false ) )
      {
        throw new IllegalStateException( "Expecting classpath as string" );
      }
      paths.addAll( Arrays.asList( strClasspath.split( "," ) ) );
    }
    _directiveInfo.setClasspaths( paths );
  }

  private void parseTypeLoaders()
  {
    List<String> loaders = new ArrayList<String>();
    while( match( Keyword.KW_typeloader, false ) )
    {
      String strLoader = parseDotPathWord();
      loaders.add( strLoader );
    }
    _directiveInfo.setTypeLoaders( loaders );
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

  public ProgramDirectiveInfo getDirectiveInfo()
  {
    return _directiveInfo;
  }
}
