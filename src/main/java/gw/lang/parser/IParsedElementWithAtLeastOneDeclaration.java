package gw.lang.parser;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.tidb.IFeatureInfoRecord;

import java.util.Stack;
import java.util.List;

/**
 * Statement that declarates a variable, e.g. VarStatement
 *
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IParsedElementWithAtLeastOneDeclaration extends IParsedElement
{
  /**
   * The offset of the token representing the name for the declaration
   * @param identifierName
   */
  int getNameOffset( CaseInsensitiveCharSequence identifierName );
  void setNameOffset( int iOffset, CaseInsensitiveCharSequence identifierName );

  /**
   * @param identifierName
   * @return True if this statement declares the given identifier; false otherwise
   */
  boolean declares(CaseInsensitiveCharSequence identifierName);
  
  /**
   * @return all names declared by this element
   */
  String[] getDeclarations();

  /**
   * @return The enclosing feature info of any variable that this statement declares @param identifierName
   */
  IFeatureInfo findOwningFeatureInfoOfDeclaredSymbols(CaseInsensitiveCharSequence identifierName);

  /**
   * @return The symbol info of the symbol at the given offset  @param enclosingFeatureInfos
   */
  List<IFeatureInfoRecord> getFeatureInfoRecordsForRepresentativeDeclaration(Stack<IFeatureInfo> enclosingFeatureInfos);
}
