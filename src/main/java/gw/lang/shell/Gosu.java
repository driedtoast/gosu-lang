package gw.lang.shell;

import gw.config.CommonServices;
import gw.config.Registry;
import gw.lang.cli.CommandLineAccess;
import gw.lang.init.ClasspathToGosuPathEntryUtil;
import gw.lang.init.GosuInitialization;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IFileContext;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IGosuProgramParser;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseResult;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.ScriptPartId;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.exceptions.ParseWarning;
import gw.lang.parser.template.ITemplateGenerator;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.GosuTemplateTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.ITemplateType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IModule;
import gw.util.GosuExceptionUtil;
import gw.util.IGosuEditor;
import gw.util.Stack;
import gw.util.StreamUtil;
import gw.util.filewatcher.FileWatcher;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class Gosu
{
  private static IGosuEditor _gosuEditor;
  private static ArgInfo _argInfo;
  private static List<File> _classpath;
  private static final List<String> SOURCE_ROOTS = Arrays.asList( "bin", "src", "test", "test-src", "classes", "gsrc", "gtest" );

  @SuppressWarnings({"ConstantConditions"})
  public static void main( String... args )
  {
    boolean error = false;
    try
    {
      _argInfo = ArgInfo.parseArgs( replaceQuotes( args ) );

      // Note: this is kind of junky, but I put all this code here so that stack traces are cleaner when coming out of
      //       the gosu runtime

      if( _argInfo.hasErrorMessage() )
      {
        showHelp( _argInfo.getErrorMessage() );
        System.exit( -1 );
      }

      if( _argInfo.getMode() == ArgInfo.Mode.VERSION )
      {
        System.out.println(getVersion());
        System.exit( 0 );
      }

      for( String propString : _argInfo.getSystemProperties() )
      {
        setSystemProperty( propString );
      }

      if( _argInfo.getMode() == ArgInfo.Mode.HELP )
      {
        showHelp( null );
        System.exit( 0 );
      }

      // init registry.xml
      File sourceFile = null;
      if( _argInfo.getSource() instanceof File )
      {
        sourceFile = (File)_argInfo.getSource();
      }
      init( sourceFile, _argInfo.getClasspath(), _argInfo.getMode() == ArgInfo.Mode.GUI );

      if( _argInfo.getMode() == ArgInfo.Mode.GUI )
      {
        FileWatcher.instance().scanForChangesAndNotify();
        launchEditor( (File)_argInfo.getSource() );
      }
      else if( _argInfo.getMode() == ArgInfo.Mode.INTERACTIVE )
      {
        new InteractiveShell(true).run();
      }
      else if( _argInfo.getMode() == ArgInfo.Mode.VERIFY )
      {
        printVerificationResults();
      }
      else if( _argInfo.getMode() == ArgInfo.Mode.EXECUTE )
      {
        // set remaining arguments as arguments to the Gosu program
        CommandLineAccess.setRawArgs( _argInfo.getArgsList() );
        String content;
        IFileContext ctx = null;
        if( _argInfo.getSource() instanceof File )
        {
          File file = (File)_argInfo.getSource();
          if( file != null && !file.getName().endsWith( ".gsp" ) )
          {
            System.err.println( "Cannot execute: " + file.getAbsoluteFile().getName() + "\nOnly Gosu program (.gsp) files are executable" );
            System.exit( -1 );
          }
          content = StreamUtil.toString( StreamUtil.getContent( new FileInputStream( file ) ) );
          ctx = new ProgramFileContext( file );
        }
        else if( _argInfo.getSource() instanceof String )
        {
          content = (String)_argInfo.getSource();
        }
        else
        {
          content = StreamUtil.toString( StreamUtil.getContent( (InputStream)_argInfo.getSource() ) );
        }
        IGosuProgramParser programParser = GosuParserFactory.createProgramParser();
        IParseResult result = programParser.parseExpressionOrProgram( content, new StandardSymbolTable( true ), new ParserOptions() );
        IGosuProgram program = result.getProgram();
        Object ret = program.getProgramInstance().evaluate(null); // evaluate it
        IType expressionType = result.getType();
        if( expressionType != null && !IJavaType.pVOID.equals( expressionType ) )
        {
          StandardSymbolTable.print( ret );
        }
      }
    }
    catch( Throwable t )
    {
      error = true;
      t.printStackTrace();
    }
    if( error )
    {
      System.exit( -1 );
    }
  }

  private static void printVerificationResults()
  {
    List<IVerificationResults> lst = verifyAllGosu( true, false );
    if( lst.size() > 0 )
    {
      for( IVerificationResults result : lst )
      {
        System.out.print( result.getTypeName() );
        System.out.println( ":" );
        System.out.println( result.getFeedback() );
      }
    }
    else
    {
      System.out.println( "No verification issues were found" );
    }
  }

  // I can't get IntellJ to pass double quotes to the JVM, it strips them
  private static String[] replaceQuotes( String[] args )
  {
    if( System.getProperty( "gw.internal.gosu.replaceQuotes" ) != null )
    {
      String[] fixedArgs = new String[args.length];
      for( int i = 0; i < args.length; i++ )
      {
        fixedArgs[i] = args[i].replace( '\'', '"' );
      }
      return fixedArgs;
    }
    else
    {
      return args;
    }
  }

  /**
   * Sets a system property, based on the supplied key=value pair. The value part is
   * optional.
   *
   * @param keyVal the key or key=value to set
   */
  private static void setSystemProperty( String keyVal )
  {
    String value = "";
    int idx = keyVal.indexOf( '=' );
    if( idx >= 0 )
    {
      value = keyVal.substring( idx + 1 );
      keyVal = keyVal.substring( 0, idx );
    }
    System.setProperty( keyVal, value );
  }

  public static void setClasspath( List<File> classpath )
  {
    addExtDir( classpath );
    removeDups( classpath );

    boolean bSameClasspath = classpath.equals( _classpath );
    _classpath = classpath;
    ClassLoader loader = TypeSystem.getCurrentModule() == null
                         // Can be null if called before the exec environment is setup, so assume the future parent of the module loader is the plugin loader
                         ? CommonServices.getEntityAccess().getPluginClassLoader()
                         : TypeSystem.getCurrentModule().getClassLoader();
    if( loader instanceof IUpdateableClassLoader )
    {
      for( File entry : classpath )
      {
        try
        {
          ((IUpdateableClassLoader)loader).addURL( entry.toURI().toURL() );
        }
        catch( MalformedURLException e )
        {
          throw new RuntimeException( e );
        }
      }
      reinitGosu( classpath );
    }

    List<String> cp = new ArrayList<String>();
    for( File f : _classpath )
    {
      cp.add( f.getAbsolutePath() );
    }

    if( !bSameClasspath )
    {
      TypeSystem.refresh( true );
    }    
  }

  private static void addExtDir( List<File> classpath )
  {
    try
    {
      URI location = Gosu.class.getProtectionDomain().getCodeSource().getLocation().toURI();
      File file = new File( location );
      if( file.exists() )
      {
        File ext = new File( file.getParentFile().getParentFile(), "ext" );
        if( ext.exists() )
        {
          for( File lib : ext.listFiles() )
          {
            if( lib.getName().endsWith( ".jar" ) )
            {
              classpath.add( lib );
            }
          }
        }
      }
    }
    catch( URISyntaxException e )
    {
      GosuExceptionUtil.forceThrow( e );
    }
  }

  private static void reinitGosu( List<File> classpath )
  {
    GosuInitialization.uninitialize();
    try
    {
      GosuInitialization.initializeRuntime( ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries( classpath ) );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }

  private static void removeDups( List<File> classpath )
  {
    for( int i = classpath.size()-1; i >= 0; i-- )
    {
      File f = classpath.get( i );
      classpath.remove( i );
      if( !classpath.contains( f ) )
      {
        classpath.add( i, f );
      }
    }
  }

  /**
   * Initializes Gosu using the classpath derived from the current classloader and system classpath.
   */
  public static void init()
  {
    init( Gosu.class );
  }

  public static void init( Class classToDeriveClasspathFrom )
  {
    init( deriveClasspathFrom( classToDeriveClasspathFrom ) );
  }

  public static void init( List<File> classpath )
  {
    init( null, classpath );
  }

  /**
   * Provides a way to initialize gosu given a particular resource
   */
  public static void init( File sourceFile, List<File> classPath )
  {
    init( sourceFile, classPath, false );
  }

  /**
   * Provides a way to initialize gosu given a particular resource
   */
  public static void init( File sourceFile, List<File> classPath, boolean inferClassPath )
  {
    classPath.addAll( initRegistry( sourceFile ) );

    if( sourceFile == null )
    {
      CommandLineAccess.setCurrentProgram( null );
      setClasspath( classPath );
    }
    else
    {
      CommandLineAccess.setCurrentProgram( sourceFile );
      if( isProgramLikeResource( sourceFile ) )
      {
        ProgramDirectiveInfo parser = ProgramDirectiveParser.parse( sourceFile );
        if( parser != null )
        {
          classPath.addAll( getClasspathFromProgram( sourceFile, parser.getClasspaths() ) );
          TypeSystem.getExecutionEnvironment().setTypeLoadersFromProgram( parser.getTypeLoaders() );
          inferClassPath = false;
        }
      }

      if( inferClassPath )
      {
        setClasspath( makeInferredClassPath( sourceFile, classPath ) );
      }
      else
      {
        setClasspath( classPath );
      }
    }
  }

  private static List<File> initRegistry( File sourceFile )
  {
    if( isProgramLikeResource( sourceFile ) )
    {
      File possibleReg = new File( sourceFile.getParentFile(), "registry.xml" );
      if( possibleReg.exists() )
      {
        try
        {
          Registry.setLocation( possibleReg.toURI().toURL() );
          ArrayList<File> files = new ArrayList<File>();
          List<String> entries = Registry.instance().getClasspathEntries();
          if( entries != null )
          {
            for( String entry : entries )
            {
              files.addAll(resolveFilesForPath( sourceFile, entry ) );
            }
          }
          return files;
        }
        catch( MalformedURLException e )
        {
          //ignore
        }
      }
    }

    // default
    Registry.setLocation( Gosu.class.getResource( "registry.xml" ) );
    return Collections.emptyList();
  }

  private static boolean isProgramLikeResource( File sourceFile )
  {
    if( sourceFile != null )
    {
      String name = sourceFile.getName();
      return !(name.endsWith( ".gs" ) || name.endsWith( ".gst" ) || name.endsWith( ".gsx" ));
    }
    else
    {
      return false;
    }
  }

  /**
   * @deprecated use init() instead
   */
  public static void initGosu( File sourceFile, List<File> classPath )
  {
    init( sourceFile, classPath );
  }

  private static List<File> makeInferredClassPath( File src, List<File> cp )
  {
    ArrayList<File> finalClassPath = new ArrayList<File>( cp );
    File sourceRoot = findSourceRoot( src );
    if( sourceRoot.exists() )
    {
      finalClassPath.add( sourceRoot );
      File projectRoot = sourceRoot.getParentFile();
      for( String name : SOURCE_ROOTS )
      {
        File file = new File( projectRoot, name );
        if( file.exists() && !finalClassPath.contains( file ) )
        {
          finalClassPath.add( file );
        }
      }
      for( String name : Arrays.asList( "jars", "lib", "support" ) )
      {
        File file = new File( projectRoot, name );
        if( file.exists() )
        {
          for( File possibleJar : file.listFiles() )
          {
            if( possibleJar.getName().endsWith( ".jar" ) && !finalClassPath.contains( possibleJar ))
            {
              finalClassPath.add( possibleJar );
            }
          }
        }
      }
    }
    return finalClassPath;
  }

  private static File findSourceRoot( File src )
  {
    src = src.getAbsoluteFile();

    File parent = null;

    // First attempt to extract the source root from the package statement
    if(src.getName().endsWith( ".gs" ) || src.getName().endsWith( ".gsx" ))
    {
      parent = extractSourceRootFromFile( src );
      if( parent != null ) {
        return parent;
      }
    }

    // If that didn't work, climb up the file hierarchy, looking for a likely root
    parent = src.getParentFile();
    
    // First climb up the parents looking for a standard source root
    while( parent != null && !SOURCE_ROOTS.contains( src.getName() ) )
    {
      parent = parent.getParentFile();
    }

    // finally, assume that the root is up two directories, as long as that isn't the system root
    parent = src.getParentFile();
    if( parent.getParentFile() == null || parent.getParentFile().getParent() == null )
    {
      return parent;
    }
    else
    {
      return parent.getParentFile();
    }
  }

  private static File extractSourceRootFromFile( File src )
  {
    try
    {
      ISourceCodeTokenizer tokenizer = CommonServices.getGosuIndustrialPark().createSourceCodeTokenizer( new FileReader( src ) );
      while( tokenizer.nextToken() != ISourceCodeTokenizer.TT_EOF )
      {
        if( tokenizer.getTokenAsString().equals( "package" ) )
        {
          Stack<String> parents = new Stack<String>();
          while( tokenizer.nextToken() == ISourceCodeTokenizer.TT_WORD )
          {
            parents.push( tokenizer.getTokenAsString() );
            if( tokenizer.nextToken() != '.' )
            {
              File parentFile = src.getParentFile();
              while( !parents.isEmpty() )
              {
                if( parentFile.getName().equals( parents.pop() ) )
                {
                  parentFile = parentFile.getParentFile();
                  if( parents.isEmpty() )
                  {
                    return parentFile;
                  }
                }
                else
                {
                  break;
                }
              }
            }
          }
        }
      }
    }
    catch( IOException e )
    {
      //ignore
    }
    return null;
  }


  public static List<IVerificationResults> verifyAllGosu( boolean includeWarnings, boolean log )
  {
    List<String> sortedNames = getAllGosuTypeNames();
    ArrayList<IVerificationResults> errors = new ArrayList<IVerificationResults>();
    int count = 0;
    int i = 0;
    int cutoff = sortedNames.size() / 10;
    DecimalFormat decimalFormat = new DecimalFormat( "#0.0" );
    if( log )
    {
      System.out.println( "Verifying" );
    }
    for( Object o : sortedNames )
    {
      i++;
      count++;
      if( i > cutoff )
      {
        i = 0;
        if( log )
        {
          double v = (double)count * 100.0;
          System.out.println( decimalFormat.format( v / (double) sortedNames.size()) + "% done." );
        }
      }
      verifyType( includeWarnings, errors, o, (CharSequence)o );
    }
    return errors;
  }

  private static List<String> getAllGosuTypeNames()
  {
    List<String> sortedNames = new ArrayList<String>();
    for( CharSequence c : TypeSystem.getTypeLoader( GosuClassTypeLoader.class ).getAllTypeNames() )
    {
      String name = c.toString();
      if( !name.startsWith( "gw." ) )
      {
        sortedNames.add( name );
      }
    }
    for( CharSequence c : TypeSystem.getTypeLoader( GosuTemplateTypeLoader.class ).getAllTypeNames() )
    {
      String name = c.toString();
      if( !name.startsWith( "gw." ) )
      {
        sortedNames.add( name );
      }
    }
    return sortedNames;
  }

  private static void verifyType( boolean includeWarnings, ArrayList<IVerificationResults> errors, Object o, CharSequence typeName )
  {
    try
    {
      IType type = TypeSystem.getByFullNameIfValid( o.toString() );
      if( type instanceof IGosuClass )
      {
        boolean valid = type.isValid();
        List<IParseIssue> parseIssues = ((IGosuClass)type).getParsedElement().getParseIssues();
        if( parseIssues.size() > 0 && (!valid || includeWarnings) )
        {
          errors.add( new GosuTypeVerificationResults( typeName.toString(), parseIssues ) );
        }
      }
      else if( type instanceof ITemplateType )
      {
        ITemplateGenerator generator = ((ITemplateType) type).getTemplateGenerator();
        try {
          generator.verify(GosuParserFactory.createParser(null));
        } catch ( ParseResultsException e) {
          errors.add( new GosuTypeVerificationResults( typeName.toString(), e.getParseIssues() ) );
        }
      }
    }
    catch( Throwable e )
    {
      errors.add( new ExceptionTypeVerificationResults( typeName.toString(), e.getMessage() ) );
    }
  }

  private static List<File> getClasspathFromProgram( File sourceFile, List<String> classpaths )
  {
    List<File> fpaths = new ArrayList<File>();
    for( String strPath : classpaths )
    {
      fpaths.addAll( resolveFilesForPath( sourceFile, strPath ) );
    }
    return fpaths;
  }

  public static Object evalProgramOrExpression( List<File> classpath, String snippet ) throws Exception
  {
    init( classpath );
    IGosuParser parser = GosuParserFactory.createParser( snippet, new StandardSymbolTable( true ) );
    return parser.parseExpOrProgram( null, false, true ).evaluate();
  }

  static void showHelp( String msg )
  {
    if( msg != null )
    {
      System.err.println( msg );
    }
    System.err.println( "usage: gosu [options] [file [args...]]" );
    System.err.println( "  -g [file]" );
    System.err.println( "      Launch the graphical editor" );
    System.err.println( "  -i" );
    System.err.println( "      Run in interactive mode (the default)" );
    System.err.println( "  -e expr" );
    System.err.println( "      The rest of the command-line is evaluated as an expression" );
    System.err.println( "  -" );
    System.err.println( "      Read from standard input" );
    System.err.println( "  file" );
    System.err.println( "      The name of a Gosu source file. Only program (.gsp) files are executable, but you can edit any Gosu source file: (<program>.gsp, <class>.gs, <enhancement>.gsx, <template>.gst" );
    System.err.println( "  args" );
    System.err.println( "      Arguments to the Gosu program" );
    System.err.println( "  -classpath cp" );
    System.err.println( "      Appends entries to the JVM's classpath" );
    System.err.println( "  -v" );
    System.err.println( "      Verify all types" );
    System.err.println( "  -version" );
    System.err.println( "      Show the version of Gosu (from VERSION.txt)" );
    System.err.println( "  -Dkey" );
    System.err.println( "  -Dkey=value" );
    System.err.println( "      Sets a system property" );
  }

  public static IGosuEditor getEditorFrame()
  {
    return _gosuEditor;
  }

  private static void launchEditor( final File file ) throws Exception
  {
    EventQueue.invokeLater(
      new Runnable()
      {
        public void run()
        {
          setClasspath( _classpath ); // necessary to set up the module correctly in the AWT thread
          _gosuEditor = CommonServices.getGosuIndustrialPark().createGosuEditor();
          _gosuEditor.openInitialFile( makePartId(file), file == null ? null : file.getAbsoluteFile() );
          _gosuEditor.showMe();
        }
      } );
    //noinspection InfiniteLoopStatement
    while( true )
    {
      Thread.sleep( Integer.MAX_VALUE ); // Wait forever. Closing GUI will cause program to exit
    }
  }

  public static IScriptPartId makePartId( File file )
  {
    IModule currentModule = TypeSystem.getCurrentModule();
    if( file == null )
    {
      return new ScriptPartId( "New Program", null );
    }
    else if( file.getName().endsWith( ".gs" ) || file.getName().endsWith( ".gsx" ) )
    {
      String classNameForFile = currentModule.getClassNameForFile( file );
      return new ScriptPartId( classNameForFile, null );
    }
    else if( file.getName().endsWith( ".gst" ) )
    {
      String templateNameForFile = currentModule.getTemplateNameForFile( file );
      return new ScriptPartId( templateNameForFile, null );
    }
    else if( file.getName().endsWith( ".gsp" ) )
    {
      String programNameForFile = currentModule.getProgramNameForFile( file );
      return new ScriptPartId( programNameForFile, null );
    }
    else
    {
      return new ScriptPartId( "Unknown Resource Type", null );
    }
  }

  private static List<File> resolveFilesForPath( File programFile, String strPath )
  {
    File file = null;
    //resolve relative paths relative to the executable, rather than the current working directory
    if( strPath.startsWith( "." ) )
    {
      String path = null;
      try
      {
        path = programFile.getCanonicalFile().getParentFile().getAbsolutePath() + File.separator + strPath;
        file = new File( path );
      }
      catch( IOException e )
      {
        throw new RuntimeException( "Could not resolve file relative to the executable with path \"" + path + "\"", e );
      }
    }

    //If it was not an obvious relative path, attempt to resolve it as an absolute path
    if( file == null )
    {
      file = new File( strPath );

      //If it is not an absolute path, try it as a (non-obvious) relative path
      if( !file.exists() )
      {
        String path = null;
        try
        {
          path = programFile.getCanonicalFile().getParentFile().getAbsolutePath() + File.separator + strPath;
          file = new File( path );
        }
        catch( IOException e )
        {
          throw new RuntimeException( "Could not resolve file relative to the executable with path \"" + path + "\"", e );
        }
      }
    }

    try {
      file = file.getCanonicalFile();
    } catch (IOException e) {
      //ignore
    }

    if( file.exists() && file.isDirectory() )
    {
      File[] files = file.listFiles( new FilenameFilter()
      {
        public boolean accept( File dir, String name )
        {
          return name.endsWith( ".jar" );
        }
      } );

      ArrayList<File> returnFiles = new ArrayList<File>();
      returnFiles.add( file );
      returnFiles.addAll( Arrays.asList( files ) );
      return returnFiles;
    }
    else
    {
      return file.exists() ? Arrays.asList( file ) : Collections.<File>emptyList();
    }
  }

  public static List<File> deriveClasspathFrom( Class clazz )
  {
    List<File> ll = new LinkedList<File>();
    ClassLoader loader = clazz.getClassLoader();
    while( loader != null )
    {
      if( loader instanceof URLClassLoader )
      {
        for( URL url : ((URLClassLoader)loader).getURLs() )
        {
          try
          {
            File file = new File( url.toURI() );
            if( file.exists() )
            {
              ll.add( file );
            }
          }
          catch( Exception e )
          {
            //ignore
          }
        }
      }
      loader = loader.getParent();
    }
    return ll;
  }

  public static List<File> getClasspath()
  {
    return _classpath;
  }

  public static String getVersion()
  {
    try
    {
      URL location = Gosu.class.getProtectionDomain().getCodeSource().getLocation();
      File jar = new File( location.toURI() );
      File versionInfo = new File( jar.getParentFile().getParentFile(), "VERSION.txt" );
      if( versionInfo.exists() && versionInfo.isFile() )
      {
        Properties props = new Properties();
        props.load( new FileReader( versionInfo ) );
        return props.getProperty( "Version" );
      }
    }
    catch( Exception e )
    {
      //ignore
    }
    return "Unknown";
  }

  public interface IVerificationResults
  {
    public boolean containsError();
    public String getFeedback();
    public String getTypeName();
  }

  private static class GosuTypeVerificationResults implements IVerificationResults
  {
    private final String _typeName;
    private final List<IParseIssue> _parseIssues;

    public GosuTypeVerificationResults( String typeName, List<IParseIssue> parseIssues )
    {
      _typeName = typeName;
      _parseIssues = parseIssues;
    }

    @Override
    public boolean containsError()
    {
      return true;
    }

    @Override
    public String getFeedback()
    {
      StringBuilder builder = new StringBuilder();
      List<ParseWarning> warnings = getWarnings();
      if( warnings.size() > 0 )
      {
        builder.append( "Warnings :\n\n" );
        for( ParseWarning warning : warnings )
        {
          builder.append( warning.getConsoleMessage() );
          builder.append( "\n" );
        }
      }
      List<ParseException> errors = getErrors();
      if( errors.size() > 0 )
      {
        builder.append( "Errors :\n\n" );
        for( ParseException error : errors )
        {
          builder.append( error.getConsoleMessage() );
          builder.append( "\n" );
        }
      }
      builder.append( "\n" );
      return builder.toString();
    }

    @Override
    public String getTypeName()
    {
      return _typeName;
    }

    public List<ParseWarning> getWarnings()
    {
      ArrayList<ParseWarning> warningArrayList = new ArrayList<ParseWarning>();
      for( IParseIssue parseIssue : _parseIssues )
      {
        if( parseIssue instanceof ParseWarning )
        {
          warningArrayList.add( (ParseWarning)parseIssue );
        }
      }
      return warningArrayList;
    }

    public List<ParseException> getErrors()
    {
      ArrayList<ParseException> warningArrayList = new ArrayList<ParseException>();
      for( IParseIssue parseIssue : _parseIssues )
      {
        if( parseIssue instanceof ParseException )
        {
          warningArrayList.add( (ParseException)parseIssue );
        }
      }
      return warningArrayList;
    }
  }

  private static class ExceptionTypeVerificationResults implements IVerificationResults
  {
    private final String _typeName;
    private final String _msg;

    public ExceptionTypeVerificationResults( String typeName, String msg )
    {
      _typeName = typeName;
      _msg = msg;
    }

    @Override
    public boolean containsError()
    {
      return true;
    }

    @Override
    public String getFeedback()
    {
      return _msg;
    }

    @Override
    public String getTypeName()
    {
      return _typeName;
    }
  }
}
