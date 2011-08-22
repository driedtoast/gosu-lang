package gw.lang.debugger;

import java.io.Serializable;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class BreakPoint implements Serializable
{
  private static boolean g_bMuted;

  private DebugLocationContext _context;
  private boolean _bActive;

  public static boolean areBreakpointsMuted()
  {
    return g_bMuted;
  }
  public static void setBreakpointsMuted( boolean bMuted )
  {
    g_bMuted = bMuted;
  }

  public BreakPoint( DebugLocationContext context )
  {
    _context = context;
    _bActive = true;
  }

  public boolean isActive()
  {
    return _bActive;
  }
  public void setActive( boolean bActive )
  {
    _bActive = bActive;
  }

  public DebugLocationContext getContext()
  {
    return _context;
  }

  public String getEnclosingType()
  {
    return _context.getEnclosingType();
  }

  public boolean equals( Object breakPoint )
  {
    if( this == breakPoint )
    {
      return true;
    }
    if( !(breakPoint instanceof BreakPoint) )
    {
      return false;
    }

    return _context.equals( ((BreakPoint)breakPoint)._context );
  }

  public int hashCode()
  {
    return _context.hashCode();
  }

  public String toString() {
    return _context.toString();
  }
}
