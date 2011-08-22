package gw.lang.shell;

import gw.util.GosuStringUtil;
import gw.util.Shell;

import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
class ArgInfo
{
  public static final String GOSU_SHELL_MODE = "gosu.shell.mode"; // system property for default command line mode
  private static final Mode DEF_GOSU_SHELL_MODE = Mode.GUI;

  static final String ERR_DASH_E_REQUIRES_AN_ARGUMENT = "-e requires an argument";
  static final String ERR_UNKNOWN_OPTION = "Unknown option: ";
  static final String ERR_MODES_CANNOT_BE_COMBINED = "Invalid command-line syntax: -g, -i and -e cannot be combined with one another";
  static final String ERR_DASH_D_REQUIRES_NAME_OF_SYSTEM_PROPERTY = "-D requires the name of a system property to set";
  static final String ERR_GUI_MODE_DOES_NOT_SUPPORT_INPUT = "GUI Mode does not support this kind of input";
  static final String ERR_GUI_MODE_DOES_NOT_SUPPORT_ARGS = "GUI Mode does not support program args";

  public static enum Mode {
    INTERACTIVE,
    GUI,
    HELP,
    VERIFY,
    VERSION,
    EXECUTE;
  }
  private Mode _mode;
  private List<File> _classpath;
  private LinkedList<String> _argsList;
  private Object _source;
  private List<String> _systemProperties;
  private String _errorMessage;

  // parse command-line arguments
  public static ArgInfo parseArgs( String... args )
  {
    ArgInfo argInfo = new ArgInfo();

    LinkedList<String> argsList = new LinkedList<String>( Arrays.asList( args ) );

    while( !argsList.isEmpty() )
    {
      String option = argsList.getFirst();
      if( option != null && option.startsWith( "-" ) )
      {
        argsList.removeFirst();
        if( option.equals( "--" ) )
        {
          break; // ignore remaining options
        }
        else if( option.equals( "-classpath" ) )
        {
          StringTokenizer st = new StringTokenizer( argsList.removeFirst(), ",", false );
          while( st.hasMoreTokens() )
          {
            String s = st.nextToken();
            if( (s.contains( ":" ) && !Shell.isWindows()) || s.contains( ";" ) )
            {
              System.err.println( "WARNING: The Gosu classpath argument should be comma separated to avoid system dependencies.\n" +
                                  "It appears you are passing in a system-dependent path delimiter" );
            }
            argInfo.getClasspath().add( new File( s ) );
          }
        }
        else if( option.startsWith( "-D" ) )
        {
          String val = option.substring( 2 );
          if( val.length() == 0 || val.indexOf( '=' ) == 0 )
          {
            argInfo._errorMessage = ERR_DASH_D_REQUIRES_NAME_OF_SYSTEM_PROPERTY;
            return argInfo;
          }
          argInfo.getSystemProperties().add( val );
        }
        else if( option.equals( "-e" ) )
        {
          verifyMode( argInfo );
          if( argsList.isEmpty() )
          {
            argInfo._errorMessage = ERR_DASH_E_REQUIRES_AN_ARGUMENT;
            return argInfo;
          }
          argInfo._source = GosuStringUtil.join( " ", argsList );
          argsList.clear();
        }
        else if( option.equals( "-" ) )
        {
          argInfo._source = System.in;
          break;
        }
        else if( option.equals( "-g" ) )
        {
          verifyMode( argInfo );
          argInfo._mode = Mode.GUI;
        }
        else if( option.equals( "-i" ) )
        {
          verifyMode( argInfo );
          argInfo._mode = Mode.INTERACTIVE;
        }
        else if( option.equals( "-h" ) || option.equals( "-?" ) || option.equals( "-help" ) || option.equals( "--help" ) )
        {
          argInfo._mode = Mode.HELP;
          return argInfo;
        }
        else if( option.equals( "-v" ) || option.equals( "-verify" ) )
        {
          if( argInfo._mode == Mode.EXECUTE )
          {
            argInfo._mode = Mode.VERIFY;
          }
        }
        else if( option.equals( "-version" ) )
        {
          argInfo._mode = Mode.VERSION;
        }
        else
        {
          argInfo._errorMessage = ERR_UNKNOWN_OPTION + option;
          return argInfo;
        }
      }
      else
      {
        break;
      }
    }
    // remaining command-line represents file to edit or program to run, along with any arguments
    if( argInfo.getSource() == null && !argsList.isEmpty() )
    {
      String option = argsList.removeFirst();
      argInfo._source = new File( option );
    }
    argInfo.getArgsList().addAll( argsList );

    if( argInfo.getMode() == Mode.GUI && !argInfo.hasErrorMessage() )
    {
      if( argInfo.getSource() != null && !(argInfo.getSource() instanceof File) )
      {
        argInfo._errorMessage = ERR_GUI_MODE_DOES_NOT_SUPPORT_INPUT;
      }
      else if( !argsList.isEmpty() )
      {
        argInfo._errorMessage = ERR_GUI_MODE_DOES_NOT_SUPPORT_ARGS;
      }
    }
    return argInfo;
  }

  private static void verifyMode(ArgInfo argInfo) {
    if( argInfo._mode != null )
    {
      argInfo._errorMessage = ERR_MODES_CANNOT_BE_COMBINED;
    }
  }

  private static Mode getDefaultMode()
  {
    String strDefMode = System.getProperty( GOSU_SHELL_MODE, DEF_GOSU_SHELL_MODE.name() );
    return Mode.valueOf( strDefMode );
  }

  ArgInfo()
  {
    _classpath = new ArrayList<File>();
    _systemProperties = new ArrayList<String>();
    _argsList = new LinkedList<String>( );
  }

  public List<File> getClasspath()
  {
    return _classpath;
  }

  public List<String> getArgsList()
  {
    return _argsList;
  }

  public Object getSource()
  {
    return _source;
  }

  public List<String> getSystemProperties()
  {
    return _systemProperties;
  }

  public Mode getMode() {
    if (_mode == null) {
      if (_source == null) {
        return getDefaultMode();
      }
      else
      {
        return Mode.EXECUTE;
      }
    }
    return _mode;
  }

  public boolean hasErrorMessage()
  {
    return _errorMessage != null;
  }

  public String getErrorMessage()
  {
    return _errorMessage;
  }
}
