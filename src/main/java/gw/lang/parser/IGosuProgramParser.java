package gw.lang.parser;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.IEvalExpression;
import gw.lang.reflect.gs.ICompilableType;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuProgramParser
{
  IParseResult parseExpressionOnly( String strSource, ISymbolTable symTable, ParserOptions options ) throws ParseResultsException;
  IParseResult parseProgramOnly( String strSource, ISymbolTable symTable, ParserOptions options) throws ParseResultsException;
  IParseResult parseExpressionOrProgram( String strSource, ISymbolTable symTable, ParserOptions options ) throws ParseResultsException;

  IParseResult parseEval( String strSource, List<ICapturedSymbol> symTable, ICompilableType enclosingClass, IEvalExpression evalExpression );
}
