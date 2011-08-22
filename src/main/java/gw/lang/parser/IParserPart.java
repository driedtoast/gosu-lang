package gw.lang.parser;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IParserPart
{
  IGosuParser getOwner();

  void setValidator( IGosuValidator validator );

  void setLineNumShift( int lineNumShift );

  int getLineNumShift();

  int getOffsetShift();
}
