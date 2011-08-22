package gw.lang.parser.template;

import gw.lang.parser.IGosuParser;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.IProgram;
import gw.lang.reflect.IType;

import java.io.Writer;
import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ITemplateGenerator
{
  String PRINT_METHOD = "printContent";

  void execute( Writer writer, ISymbolTable symbolTable ) throws TemplateParseException;

  void execute( Writer writer, StringEscaper escaper, ISymbolTable symTable ) throws TemplateParseException;

  void compile( ISymbolTable symTable ) throws TemplateParseException;

  void verify( IGosuParser parser ) throws ParseResultsException;

  boolean isValid();

  List<ISymbol> getParameters();

  IType getSuperType();

  String getFullyQualifiedTypeName();

  List<TemplateParseException> getTemplateSyntaxProblems();

  public IProgram getProgram();

  public String getSource();
}
