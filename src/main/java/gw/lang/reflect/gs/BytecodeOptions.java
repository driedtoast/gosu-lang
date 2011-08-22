package gw.lang.reflect.gs;

/**
 * The BytecodeOptions class controls simple options around bytecode compilation.  For the most part these options
 * are changed during language development to enabled additional debugging or to conditionally enable new features.
 * Some of the options, however, can be dynamically enabled or disabled depending on the environment.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class BytecodeOptions
{
  public static boolean SINGLE_SERVING_LOADER = false;
  private static boolean AGGRESSIVELY_VERIFY = false;
  private static boolean CAN_RELOAD_CLASSES = determineIfCanReloadClasses();
  private static boolean COLLECT_COMPILATION_STATISTICS = false;
  private static boolean GENERATE_ANNOTATIONS_TO_CLASS_FILES = true;
  private static boolean TREE_VERIFICATION_ENABLED = false;

  public static final String RELOAD_CLASSES_SYSTEM_PROP = "gosu.can.reload.classes";

    public static void enableAggressiveVerification()
  {
    AGGRESSIVELY_VERIFY = true;
  }

  public static boolean isSingleServingLoader()
  {
    return SINGLE_SERVING_LOADER;
  }

  public static void setSingleServingLoader( boolean bSingleServingLoader )
  {
    SINGLE_SERVING_LOADER = bSingleServingLoader;
  }

  public static boolean aggressivelyVerify()
  {
    return AGGRESSIVELY_VERIFY;
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public static boolean shouldDebug( String strClass )
  {
    return false;
  }

  public static boolean canReloadClasses()
  {
    return CAN_RELOAD_CLASSES;
  }
  public static void setCanReloadClasses( boolean bReload )
  {
    CAN_RELOAD_CLASSES = bReload;
  }

  public static boolean isGenerateAnnotationsToClassFiles()
  {
    return GENERATE_ANNOTATIONS_TO_CLASS_FILES;
  }

  public static void setGenerateAnnotationsToClassFiles( boolean genAnnotations )
  {
    GENERATE_ANNOTATIONS_TO_CLASS_FILES = genAnnotations;
  }

  public static boolean collectCompilationStatistics()
  {
    return COLLECT_COMPILATION_STATISTICS;
  }

  private static boolean determineIfCanReloadClasses() {
    String explicitValue = System.getProperty(RELOAD_CLASSES_SYSTEM_PROP);
    if (explicitValue != null) {
      if (explicitValue.toLowerCase().equals("true")) {
        return true;
      } else if (explicitValue.toLowerCase().equals("false")) {
        return false;
      } else {
        System.out.println("WARNING: unrecognized value " + explicitValue + " found for system property " + RELOAD_CLASSES_SYSTEM_PROP + ".  The value must be either 'true' or 'false'.");
      }
    }
    return System.getProperty( "java.vm.name" ).contains( "Dynamic Code Evolution" );
  }

  public static boolean isTreeVerifcationEnabled()
  {
    return TREE_VERIFICATION_ENABLED;
  }

  public static void setTreeVerificationEnabled( boolean b )
  {
    TREE_VERIFICATION_ENABLED = b;
  }
}
