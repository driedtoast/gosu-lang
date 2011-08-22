package gw.lang.debugger;

import gw.lang.parser.IParseTree;

import java.io.Serializable;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class DebugLocationContext implements Serializable
{
  private IDebugContextProperties _context;
  private int _iLine = -1;
  private int _offset;

  public DebugLocationContext( IDebugContextProperties context, IParseTree location )
  {
    _context = context;
    if( location == null )
    {
      _iLine = -1;
      _offset = -1;
    }
    else
    {
      _iLine = location.getLineNum();
      _offset = location.getOffset();
    }
  }

  public DebugLocationContext( IDebugContextProperties context, int iLine, int iOffset )
  {
    _context = context;
    _iLine = iLine;
    _offset = iOffset;
  }

  /**
   * Location can be null. If so, the location is considered to be the beginning
   * of the context e.g., for a program this would be the main entry point into
   * the program.
   */
  public DebugLocationContext( IDebugContextProperties context )
  {
    _context = context;
  }


  public int getLineNum()
  {
    return _iLine;
  }

  public void setLineNum( int iLine )
  {
    _iLine = iLine;
  }

  public int getOffset()
  {
    return _offset;
  }

  public void setOffset( int iOffset )
  {
    _offset = iOffset;
  }

  public IDebugContextProperties getContext()
  {
    return _context;
  }

  public void setContext( IDebugContextProperties context )
  {
    _context = context;
  }

  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( !(o instanceof DebugLocationContext) )
    {
      return false;
    }

    DebugLocationContext debugLocationContext = (DebugLocationContext)o;

    if( !_context.equals( debugLocationContext._context ) )
    {
      return false;
    }

    //return _location.areOffsetAndExtentEqual( debugLocationContext._location );
    return getLineNum() == debugLocationContext.getLineNum();
  }

  public int hashCode()
  {
    int result;
    result = _context.hashCode();
    result = 29 * result + _iLine;
    return result;
  }

  public String toString()
  {
    return getEnclosingType() + " : " + getLineNum();
  }

  public String getBreakpointTitle()
  {
    if( _iLine > 0 )
    {
      return "Line " + _iLine + " in " + _context.getBreakpointTitle();
    }
    return _context.getBreakpointTitle();
  }

  public String getTitle()
  {
    if( _iLine != -1 )
    {
      return " Line " + _iLine;
    }
    return "";
  }

  public boolean hasLineNumber()
  {
    return _iLine > 0;
  }

  public String getEnclosingType()
  {
    return _context.getEnclosingType();
  }
}
