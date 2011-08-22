package gw.lang.reflect.gs;

import gw.lang.parser.IGosuParser;
import gw.lang.parser.IHasInnerClass;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;

/**
 * ICompilableType represents a type that can be compiled into a Class at runtime.  Currently there are
 * only two varieties of ICompilableTypes:  IGosuClass and IGosuFragment.  This interface allows us to
 * directly compile fragments into classes at runtime without implementing the full contract of IGosuClass.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ICompilableType extends IType, IHasInnerClass {

  ICompilableType getEnclosingType();

  GosuClassTypeLoader getTypeLoader();

  IRelativeTypeInfo getTypeInfo();

  boolean isAnonymous();

  ISymbol getExternalSymbol(String s);

  ISourceFileHandle getSourceFileHandle();

  ITypeUsesMap getTypeUsesMap();

  IType resolveRelativeInnerClass( String strTypeName, boolean bForce );

  boolean isStatic();

  IGosuParser getEditorParser();

  IClassStatement getClassStatement();

  IGosuClass getBlock(int i);

}
