package gw.lang.parser;

import java.io.IOException;
import java.io.Reader;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ISourceCodeTokenizer
{
  int TT_EOL = '\n';
  int TT_EOF = -1;
  int TT_WHITESPACE = -2;
  int TT_COMMENT = -3;
  int TT_NUMBER = -4;
  int TT_WORD = -5;
  int TT_OPERATOR = -6;
  int TT_KEYWORD = -7;
  int TT_NOTHING = -8;
  int TT_INTEGER = -9;

  ISourceCodeTokenizer copy();

  ISourceCodeTokenizer lightweightRestore();

  boolean isPositioned();

  void reset();

  void reset( Reader reader );

  void reset( SourceCodeReader reader );

  void resetSyntax();

  SourceCodeReader getReader();

  String getSource();

  ITokenizerInstructor getInstructor();

  void setInstructor( ITokenizerInstructor instructor );

  int getInstruction();

  boolean isWhitespaceSignificant();

  void setWhitespaceSignificant( boolean bWhitespaceSignificant );

  boolean isCommentsSignificant();

  void setCommentsSignificant( boolean bCommentsSignificant );

  int getLineStart();

  int getLineNumber();

  void setLineNumber( int lineNumber );

  void setLineOffset( int lineOffset );

  int getLineOffset();

  int getTokenColumn();

  void wordChars( int iLow, int iHigh );

  void whitespaceChars( int iLow, int iHigh );

  void ordinaryChars( int iLow, int iHigh );

  void ordinaryChar( int ch );

  void operators( String[] astrOperators );

  void operatorChars( int iLow, int iHigh );

  boolean isOperator( String strOperator );

  void commentChar( int ch );

  void quoteChar( int ch );

  void parseNumbers();

  void eolIsSignificant( boolean bFlag );

  void slashStarComments( boolean bFlag );

  void slashSlashComments( boolean bFlag );

  void lowerCaseMode( boolean bLowerCaseMode );

  boolean isUnterminatedString();

  boolean isUnterminatedComment();

  void setParseDotsAsOperators( boolean parseDotsAsOperators );

  boolean isParseDotsAsOperators();

  int getTokenStart();

  int getTokenEnd();

  String getTokenAsString();

  IState mark();

  void restoreToMark( IState markedState );

  void setRestoreState( IState state );

  IState copyRestoreState( ISourceCodeTokenizer owner );

  IState getRestoreState();

  int nextToken() throws IOException;

  void pushBack();

  void pushBack(int iType, String token);

  void pushOffsetMarker( ITokenizerOffsetMarker offsetMarker );
  void popOffsetMarker( ITokenizerOffsetMarker offsetMarker );

  IState getState();

  int countMatches( String s );

  int countMatches( String s, int tokenType );

  int getType();

  String getStringValue();

  void swallowPragmaIfNecessary();
    
  boolean isEOF();

  public interface IState extends Cloneable
  {
    Object clone( ISourceCodeTokenizer owner );
  }
}
