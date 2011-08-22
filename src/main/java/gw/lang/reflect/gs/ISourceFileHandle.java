package gw.lang.reflect.gs;

import gw.lang.parser.ParserSource;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ISourceFileHandle
{
  public ParserSource getSource();

  public String getParentType();

  String getNamespace();

  String getResourceName();

  boolean isTestClass();

  boolean isValid();

  public void cleanAfterCompile();

  void cleanAfterVerification();

  ClassType getClassType();

  String getTypeNamespace();

  String getRelativeName();

  Object getUserData();
  void setUserData( Object data );

  void setOffset( int iOffset );
  int getOffset();
  void setEnd( int iEnd );
  int getEnd();

  /**
   * @return a relative representation of the file appropriate for debugging/stack traces
   */
  String getFileRef();

  long getFileTimestamp();
}
