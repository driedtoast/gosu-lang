/* 
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.IModifierInfo;


/**
 * Defines an interface to represent symbols in an ISymbolTable.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ISymbol extends IHasType
{
  public static final Object NULL_DEFAULT_VALUE = new Object() { public String toString() { return "<null-default-value>"; } };

  CaseInsensitiveCharSequence THIS = CaseInsensitiveCharSequence.get( Keyword.KW_this );
  CaseInsensitiveCharSequence SUPER = CaseInsensitiveCharSequence.get( Keyword.KW_super );

  /**
   * Returns the Symbol's name.
   */
  public String getName();

  /**
   * Returns the Symbol's optional display name.  If a display name is not assigned,
   * returns the symbol's name.
   */
  public String getDisplayName();

  /**
   * Returns a case-insensitive char sequence. This is primarily for faster
   * lookups in IScope.
   */
  public CaseInsensitiveCharSequence getCaseInsensitiveName();

  /**
   * Returns the Symbol's type.
   */
  public IType getType();

  /**
   * Sets the Symbol's type.
   */
  public void setType( IType type );

  /**
   * Returns the value assigned to this Symbol.
   */
  public Object getValue();

  /**
   * Assigns a value to this Symbol.
   */
  public void setValue( Object value );

  /**
   * The symbol's default value e.g., a default parameter value for a function.
   */
  public Object getDefaultValue();
  public void setDefaultValue( Object defaultValue );

  /**
   * Assigns an optional symbol table so that the symbol can get/set its value
   * dynamically e.g., via ThreadLocalSymbolTable.
   */
  public void setDynamicSymbolTable( ISymbolTable symTable );

  public boolean hasDynamicSymbolTable();
  
  public ISymbolTable getDynamicSymbolTable();

  /**
   * Creates a copy of this symbol without the value so that the empty symbol can be stored.
   */
  public ISymbol getLightWeightReference();

  /**
   * Returns true if this symbol is writable.
   * <p/>
   * An example of a symbol that is not writable is a readonly Property
   * referenced as a symbol in a Gosu class.
   */
  public boolean isWritable();

  /**
   * Indicates that this symbol should use a reference rather than storing its value directly.
   */
  void setValueIsBoxed( boolean b );

  /**
   * @return the reference to this symbols value.
   *
   * @throws IllegalStateException if symbol is not marked as shouldUseReference
   */
  BoxedValue getBoxedValue();

  int getIndex();

  void initValue(Object initValue);

  boolean canBeCaptured();

  ICapturedSymbol makeCapturedSymbol(CaseInsensitiveCharSequence strInsensitiveName, String strName, ISymbolTable symbolTable, IScope scope);

  public boolean isValueBoxed();
  
  boolean isLocal();

  IModifierInfo getModifierInfo();
}
