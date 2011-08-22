package gw.lang.reflect;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class Modifier extends java.lang.reflect.Modifier
{
  /**
   * The <code>int</code> value representing the <code>override</code> modifier.
   */
  public static final int OVERRIDE = 0x00010000;

  /**
   * The <code>int</code> value representing the <code>hide</code> modifier.
   */
  public static final int HIDE = 0x00020000;

  /**
   * The code indicating something is a class member
   */
  public static final int CLASS_MEMBER = 0x00040000;

  /**
   * The <code>int</code> value representing the <code>internal</code> modifier.
   */
  public static final int INTERNAL = 0x00080000;

  /**
   * The <code>int</code> value representing the <code>enum</code> modifier.  This value should match
   * the Java version of the enum modifier (which isn't publically exposed).  Note that the enum modifier
   * may mean different things in different contexts.
   */
  public static final int ENUM = 0x00004000; // Match the Java value for the enum modifier

  public static int getModifiersFrom( IAttributedFeatureInfo afi )
  {
    int iModifiers = 0;
    iModifiers = Modifier.setBit( iModifiers, afi.isPublic(), PUBLIC );
    iModifiers = Modifier.setBit( iModifiers, afi.isPrivate(), PRIVATE );
    iModifiers = Modifier.setBit( iModifiers, afi.isProtected(), PROTECTED );
    iModifiers = Modifier.setBit( iModifiers, afi.isInternal(), INTERNAL );
    iModifiers = Modifier.setBit( iModifiers, afi.isStatic(), STATIC );
    return iModifiers;
  }

  /**
   * Return <tt>true</tt> if the integer argument includes the
   * <tt>hide</tt> modifer, <tt>false</tt> otherwise.
   *
   * @param mod a set of modifers
   *
   * @return <tt>true</tt> if <code>mod</code> includes the
   *         <tt>hide</tt> modifier; <tt>false</tt> otherwise.
   */
  public static boolean isHide( int mod )
  {
    return (mod & HIDE) != 0;
  }

  /**
   * Return <tt>true</tt> if the integer argument includes the
   * <tt>override</tt> modifer, <tt>false</tt> otherwise.
   *
   * @param mod a set of modifers
   *
   * @return <tt>true</tt> if <code>mod</code> includes the
   *         <tt>override</tt> modifier; <tt>false</tt> otherwise.
   */
  public static boolean isOverride( int mod )
  {
    return (mod & OVERRIDE) != 0;
  }

  /**
   * Return <tt>true</tt> if the integer argument includes the
   * <tt>class member</tt> modifer, <tt>false</tt> otherwise.
   *
   * @param mod a set of modifers
   *
   * @return <tt>true</tt> if <code>mod</code> includes the
   *         <tt>class member</tt> modifier; <tt>false</tt> otherwise.
   */
  public static boolean isClassMember( int mod )
  {
    return (mod & CLASS_MEMBER) != 0;
  }

  /**
   * Return <tt>true</tt> if the integer argument includes the
   * <tt>internal</tt> modifer, <tt>false</tt> otherwise.
   *
   * @param mod a set of modifers
   *
   * @return <tt>true</tt> if <code>mod</code> includes the
   *         <tt>internal</tt> modifier; <tt>false</tt> otherwise.
   */
  public static boolean isInternal( int mod )
  {
    return (mod & INTERNAL) != 0;
  }

   /**
   * Return <tt>true</tt> if the integer argument includes the
   * <tt>enum</tt> modifer, <tt>false</tt> otherwise.
   *
   * @param mod a set of modifers
   *
   * @return <tt>true</tt> if <code>mod</code> includes the
   *         <tt>enum</tt> modifier; <tt>false</tt> otherwise.
   */
  public static boolean isEnum( int mod ) {
    return (mod & ENUM) != 0;
  }

  public static int setPublic( int mod, boolean bValue )
  {
    return setBit( mod, bValue, PUBLIC );
  }

  public static int setPrivate( int mod, boolean bValue )
  {
    return setBit( mod, bValue, PRIVATE );
  }

  public static int setProtected( int mod, boolean bValue )
  {
    return setBit( mod, bValue, PROTECTED );
  }

  public static int setStatic( int mod, boolean bValue )
  {
    return setBit( mod, bValue, STATIC );
  }

  public static int setAbstract( int mod, boolean bValue )
  {
    return setBit( mod, bValue, ABSTRACT );
  }

  public static int setFinal( int mod, boolean bValue )
  {
    return setBit( mod, bValue, FINAL );
  }

  public static int setOverride( int mod, boolean bValue )
  {
    return setBit( mod, bValue, OVERRIDE );
  }

  public static int setHide( int mod, boolean bValue )
  {
    return setBit( mod, bValue, HIDE );
  }

  public static int setClassMember( int mod, boolean bValue )
  {
    return setBit( mod, bValue, CLASS_MEMBER );
  }

  public static int setTransient( int mod, boolean bValue )
  {
    return setBit( mod, bValue, TRANSIENT );
  }

  public static int setInternal( int mod, boolean bValue )
  {
    return setBit( mod, bValue, INTERNAL );
  }

  public static int setEnum( int mod, boolean bValue )
  {
    return setBit( mod, bValue, ENUM );
  }

  private static int setBit( int mod, boolean bValue, int bit )
  {
    if( bValue )
    {
      return mod |= bit;
    }
    else
    {
      return mod &= ~bit;
    }
  }
}
