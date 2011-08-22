/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package gw.lang.parser;

import gw.lang.parser.statements.IUsesStatementList;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.expressions.IProgram;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ISourceFileHandle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * IGosuParser provides an interface for dynamically parsing and executing
 * <b>G</b>uidewire <b>Script</b> (Gosu) - a JavaScript-like language.
 * <p/>
 * The following context-free grammar (BNF) specifies the syntax of Gosu.
 * Note the grammar the parser implementation models is LL(1) -- it has
 * left-recursion removed and is fully left factored. The following grammar,
 * however, is provided as-is for better readability.
 * <p/>
 * <pre>
 * ========================================================================
 *    Expressions
 * ========================================================================
 * <p/>
 * <i>primary-expression</i>
 *   <b>null</b>
 *   <b>true</b>
 *   <b>false</b>
 *   <b>NaN</b>
 *   <b>Infinity</b>
 *   &lt;member-access&gt;
 *   &lt;array-access&gt;
 *   &lt;name&gt;
 *   &lt;literal&gt;
 *   &lt;new-expression&gt;
 *   <b>(</b> &lt;expression&gt; <b>)</b>
 *   &lt;method-call-expression&gt;
 *   &lt;exists-expression&gt;
 *   &lt;typeis-expression&gt;
 *   &lt;cast-expression&gt;
 *   &lt;query-expression&gt;
 * <p/>
 * <i>name</i>
 *   &lt;identifier&gt;
 * <p/>
 * <i>member-access</i>
 *   &lt;root-expression&gt;.&lt;member&gt;
 *   &lt;root-expression&gt;[member-name]
 * <p/>
 * <i>root-expression</i>
 *   &lt;bean-reference&gt;
 *   &lt;type-literal&gt;
 * <p/>
 * <i>array-access</i>
 *   &lt;primary-expression&gt;[member-index]
 * <p/>
 * <i>member</i>
 *   &lt;member-access&gt;
 *   &lt;identifier&gt;
 * <p/>
 * <i>bean-reference</i>
 *   &lt;primary-expression&gt;
 * <p/>
 * <i>member-name</i>
 *   &lt;expression&gt;
 * <p/>
 * <i>member-index</i>
 *   &lt;expression&gt;
 * <p/>
 * <i>identifier</i>
 *   underscore
 *   unicode-char
 *   &lt;identifier&gt; decimal-digit
 * <p/>
 * <i>type-name</i>
 *   &lt;identifier&gt;
 * <p/>
 * <i>type-literal</i>
 *   &lt;type-name&gt; [<b>&lt;</b> &lt;type-parameter-list&gt; <b>&gt;</b>] [ <b>[]</b> ]
 * <p/>
 * <i>type-parameter-list</i>
 *   &lt;type-parameter&gt;
 *   &lt;type-parameter-list&gt; , &lt;type-parameter&gt;
 * <p/>
 * <i>type-parameter</i>
 *   <b>?</b> [<b>extends</b> &lt;type-literal&gt;]
 *   &lt;type-literal&gt;
 * <p/>
 * <i>new-expression</i>
 *   <b>new</b> &lt;type-expression&gt; <b>(</b> [argument-list] <b>)</b>
 *   <b>new</b> &lt;type-expression&gt; <b>[</b> &lt;expression&gt; <b>]</b>
 *   <b>new</b> &lt;type-expression&gt; <b>[</b><b>]</b> <b>{</b> [array-value-list] <b>}</b>
 * <p/>
 * <i>array-value-list</i>
 *   &lt;expression&gt;
 *   &lt;array-value-list&gt; , &lt;expression&gt;
 * <p/>
 * <i>method-call-expression</i>
 *   &lt;name&gt; <b>(</b> [argument-list] <b>)</b>
 *   &lt;member-access&gt; <b>(</b> [argument-list] <b>)</b>
 * <p/>
 * <i>argument-list</i>
 *   &lt;expression&gt;
 *   &lt;argument-list&gt; , &lt;expression&gt;
 * <p/>
 * <i>exists-expression</i>
 *   <b>exists</b> <b>(</b> &lt;identifier&gt; <b>in</b> &lt;expression&gt; [ <b>index</b> &lt;identifier&gt; ] <b>where</b> &lt;expression&gt; <b>)</b>
 * <p/>
 * <i>cast-expression</i>
 *   &lt;expression&gt; <b>as</b> &lt;type-literal&gt;
 * <p/>
 * <i>unary-expression</i>
 *   <b>+</b> &lt;unary-expression&gt;
 *   <b>-</b> &lt;unary-expression&gt;
 *   &lt;unary-expression-not-plus-minus&gt;
 * <p/>
 * <i>unary-expression-not-plus-minus</i>
 *   &lt;primary-expression&gt;
 *   <b>~</b> &lt;unary-expression&gt;
 *   <b>!</b> &lt;unary-expression&gt;
 *   <b>not</b> &lt;unary-expression&gt;
 *   <b>typeof</b> &lt;unary-expression&gt;
 *   <b>eval</b> <b>(</b> &lt;expression&gt; <b>)</b>
 * <p/>
 * <i>typeis-expression</i>
 *   &lt;conditional-or-expression&gt; <b>typeis</b> &lt;type-literal&gt;
 * <p/>
 * <i>multiplicative-expression</i>
 *   &lt;unary-expression&gt;
 *   &lt;multiplicative-expression&gt; <b>*</b> &lt;unary-expression&gt;
 *   &lt;multiplicative-expression&gt; <b>/</b> &lt;unary-expression&gt;
 *   &lt;multiplicative-expression&gt; <b>%</b> &lt;unary-expression&gt;
 * <p/>
 * <i>additive-expression</i>
 *   &lt;multiplicative-expression&gt;
 *   &lt;additive-expression&gt; <b>+</b> &lt;multiplicative-expression&gt;
 *   &lt;additive-expression&gt; <b>-</b> &lt;multiplicative-expression&gt;
 * <p/>
 * <i>relational-expression</i>
 *   &lt;additive-expression&gt;
 *   &lt;relational-expression&gt; <b>&lt;</b> &lt;additive-expression&gt;
 *   &lt;relational-expression&gt; <b>&gt;</b> &lt;additive-expression&gt;
 *   &lt;relational-expression&gt; <b>&lt;=</b> &lt;additive-expression&gt;
 *   &lt;relational-expression&gt; <b>&gt;=</b> &lt;additive-expression&gt;
 * <p/>
 * <i>equality-expression</i>
 *   &lt;relational-expression&gt;
 *   &lt;equality-expression&gt; <b>==</b> &lt;relational-expression&gt;
 *   &lt;equality-expression&gt; <b>!=</b> &lt;relational-expression&gt;
 *   &lt;equality-expression&gt; <b>&lt;&gt;</b> &lt;relational-expression&gt;
 * <p/>
 * <i>conditional-and-expression</i>
 *   &lt;equality-expression&gt;
 *   &lt;conditional-and-expression&gt; <b>&&</b> &lt;equality-expression&gt;
 *   &lt;conditional-and-expression&gt; <b>and</b> &lt;equality-expression&gt;
 * <p/>
 * <i>conditional-or-expression</i>
 *   &lt;conditional-and-expression&gt;
 *   &lt;conditional-or-expression&gt; <b>||</b> &lt;conditional-and-expression&gt;
 *   &lt;conditional-or-expression&gt; <b>or</b> &lt;conditional-and-expression&gt;
 * <p/>
 * <i>conditional-expression</i>
 *   &lt;conditional-or-expression&gt;
 *   &lt;conditional-or-expression&gt; <b>?</b> &lt;conditional-expression&gt; <b>:</b> &lt;conditional-expression&gt;
 *   &lt;typeis-expression&gt;
 * <p/>
 * <i>expression</i>
 *   &lt;conditional-expression&gt;
 * <p/>
 * <p/>
 * ========================================================================
 *    Query Expression
 * ========================================================================
 * <p/>
 * <i>query-expression</i>
 *   <b>find</b> <b>(</b> &lt;identifier&gt; <b>in</b> &lt;query-path-expression&gt; <b>where</b> &lt;where-clause-expression&gt; <b>)</b>
 * <p/>
 * <i>exists-expression</i>
 *   <b>exists</b> <b>(</b> &lt;identifier&gt; <b>in</b> &lt;query-path-expression&gt; <b>where</b> &lt;where-clause-expression&gt; <b>)</b>
 * <p/>
 * <i>where-clause-expression</i>
 *   &lt;where-clause-conditional-expression&gt;
 * <p/>
 * <i>where-clause-conditional-expression</i>
 *   &lt;where-clause-conditional-or-expression&gt;
 * <p/>
 * <i>where-cluase-conditional-or-expression</i>
 *   &lt;where-cluase-conditional-and-expression&gt;
 *   &lt;where-cluase-conditional-or-expression&gt; <b>||</b> &lt;where-cluase-conditional-and-expression&gt;
 *   &lt;where-cluase-conditional-or-expression&gt; <b>or</b> &lt;where-cluase-conditional-and-expression&gt;
 * <p/>
 * <i>where-clause-conditional-and-expression</i>
 *   &lt;where-clause-equality-expression&gt;
 *   &lt;where-clause-conditional-and-expression&gt; <b>&&</b> &lt;where-clause-equality-expression&gt;
 *   &lt;where-clause-conditional-and-expression&gt; <b>and</b> &lt;where-clause-equality-expression&gt;
 * <p/>
 * <i>where-clause-equality-expression</i>
 *   &lt;where-clause-relational-expression&gt;
 *   &lt;where-clause-equality-expression&gt; <b>==</b> &lt;relational-expression&gt;
 *   &lt;where-clause-equality-expression&gt; <b>!=</b> &lt;relational-expression&gt;
 *   &lt;where-clause-equality-expression&gt; <b>&lt;&gt;</b> &lt;relational-expression&gt;
 * <p/>
 * <i>where-clause-relational-expression</i>
 *   &lt;where-clause-unary-expression&gt;
 *   &lt;where-clause-relational-expression&gt; <b>&lt;</b> &lt;additive-expression&gt;
 *   &lt;where-clause-relational-expression&gt; <b>&gt;</b> &lt;additive-expression&gt;
 *   &lt;where-clause-relational-expression&gt; <b>&lt;=</b> &lt;additive-expression&gt;
 *   &lt;where-clause-relational-expression&gt; <b>&gt;=</b> &lt;additive-expression&gt;
 *   &lt;where-clause-relational-expression&gt; <b>in</b> &lt;where-clause-in-expression&gt;
 *   &lt;where-clause-relational-expression&gt; <b>startswith</b> &lt;where-clause-in-expression&gt;
 *   &lt;where-clause-relational-expression&gt; <b>contains</b> &lt;where-clause-in-expression&gt;
 * <p/>
 * <p/>
 * <i>where-clause-in-expression</i>
 *   &lt;expression&gt;
 * <p/>
 * <i>where-clause-unary-expression</i>
 *   &lt;where-clause-primary-expression&gt;
 *   <b>!</b> &lt;where-clause-unary-expression&gt;
 *   <b>not</b> &lt;where-clause-unary-expression&gt;
 * <p/>
 * <i>where-clause-primary-expression</i>
 *   &lt;exists-expression&gt;
 *   &lt;query-path-expression&gt;
 *   <b>(</b> &lt;where-clause-expression&gt; <b>)</b>
 * <p/>
 * <p/>
 * ========================================================================
 *    Statements
 * ========================================================================
 * <p/>
 * <i>statement</i>
 *   &lt;uses-statement&gt;
 *   &lt;namespace-statement&gt;
 *   &lt;block&gt;
 *   &lt;assignment-statement&gt;
 *   &lt;method-call-statement&gt;
 *   &lt;if-statement&gt;
 *   &lt;for...in-statement&gt;
 *   &lt;while-statement&gt;
 *   &lt;do...while-statement&gt;
 *   &lt;switch-statement&gt;
 *   &lt;var-statement&gt;
 *   &lt;continue-statement&gt;
 *   &lt;break-statement&gt;
 *   &lt;return-statement&gt;
 *   &lt;semi-colon-statement&gt;
 *   &lt;class-statement&gt;
 *   &lt;function-definition&gt;
 *   &lt;try-catch-finally-statement&gt;
 *   &lt;throw-statement&gt;
 * <p/>
 * <i>namespace-statement</i>
 *   <b>package</b> &lt;package-name&gt;
 * <p/>
 * <i>uses-statement</i>
 *   <b>uses</b> type-literal
 *   <b>uses</b> namespace-ref
 * <p/>
 * <i>namespace-ref</i>
 *   namespace-name .*
 * <p/>
 * <i>block</i>
 *   <b>{</b> [statement-list] <b>}</b>
 * <p/>
 * <i>statement-list</i>
 *   &lt;statement&gt;
 *   &lt;statement-list&gt; &lt;statement&gt;
 * <p/>
 * <i>assignment-statement</i>
 *   &lt;identifier&gt; <b>=</b> &lt;expression&gt;
 *   &lt;member-access&gt; <b>=</b> &lt;expression&gt;
 *   &lt;array-access&gt; <b>=</b> &lt;expression&gt;
 * <p/>
 * <i>method-call-statement</i>
 *   &lt;method-call-expression&gt;
 * <p/>
 * <i>if-statement</i>
 *   <b>if</b> <b>(</b> &lt;expression&gt; <b>)</b> &lt;statement&gt; [ <b>else</b> &lt;statement&gt; ] [ <b>unless</b> <b>(</b> &lt;expression&gt; <b>)</b> ]
 * <p/>
 * <i>for...in-statement</i>
 *   <b>for</b> <b>(</b> &lt;identifier&gt; <b>in</b> &lt;expression&gt; [ <b>index</b> &lt;identifier&gt; ] <b>)</b> &lt;statement&gt;
 * <p/>
 * <i>while-statement</i>
 *   <b>while</b> <b>(</b> &lt;expression&gt; <b>)</b> &lt;statement&gt;
 * <p/>
 * <i>do...while-statement</i>
 *   <b>do</b> &lt;statement&gt; <b>while</b> <b>(</b> &lt;expression&gt; <b>)</b>
 * <p/>
 * <i>switch-statement</i>
 *   <b>switch</b> <b>(</b>&lt;expression&gt;<b>) {</b> [switch-cases] [switch-default] <b>}</b>
 * <p/>
 * <i>switch-cases</i>
 *   &lt;switch-case&gt;
 *   &lt;switch-cases&gt; &lt;switch-case&gt;
 * <p/>
 * <i>switch-case</i>
 *   <b>case</b> &lt;expression&gt; <b>:</b> [statement-list]
 * <p/>
 * <i>switch-default</i>
 *   <b>default</b> <b>:</b> [statement-list]
 * <p/>
 * <i>var-statement</i>
 *   <b>var</b> &lt;identifier&gt; [scope-attribute] [ <b>:</b> &lt;type-expression&gt; ] <b>=</b> &lt;expression&gt;
 *   <b>var</b> &lt;identifier&gt; [scope-attribute] <b>:</b> &lt;type-expression&gt; [ <b>=</b> &lt;expression&gt; ]
 *                                                                                              \
 * <i>continue-statement</i>
 *   <b>continue</b>
 * <p/>
 * <i>break-statement</i>
 *   <b>break</b>
 * <p/>
 * <i>return-statement</i>
 *   <b>return</b> &lt;expression&gt;
 *   <b>return</b> <b>;</b>
 * <p/>
 * <i>semi-colon-statement</i>
 *   <b>;</b>
 * <p/>
 * <i>class-statement</i>
 *   [modifiers] <b>class</b> &lt;identifier&gt; [<b>extends</b> &lt;base-class&gt;] [<b>implements</b> &lt;interfaces-list&gt;] { &lt;class-members&gt; }
 * <p/>
 * <i>function-definition</i>
 *   [modifiers] <b>function</b> &lt;identifier&gt; ( [ &lt;argument-declaration-list&gt; ] ) [ : &lt;type-literal&gt; ] &lt;statement-block&gt;
 * <p/>
 * <i>class-members</i>
 *   &lt;class-member&gt;
 *   &lt;class-members&gt; &lt;class-member&gt;
 * <p/>
 * <i>class-member</i>
 *   &lt;field-statement&gt;
 *   &lt;constructor-statement&gt;
 *   &lt;method-statement&gt;
 *   &lt;property-statement&gt;
 * <p/>
 * <i>field-statement</i>
 *   [modifiers] <b>var</b> &lt;identifier&gt; [scope-attribute] [ <b>:</b> &lt;type-expression&gt; ] <b>=</b> &lt;expression&gt;
 *   [modifiers] <b>var</b> &lt;identifier&gt; [scope-attribute] <b>:</b> &lt;type-expression&gt; [ <b>=</b> &lt;expression&gt; ]
 *   [modifiers] <b>var</b> &lt;identifier&gt; [scope-attribute] <b>:</b> &lt;type-expression&gt; [ <b>as</b> [<b>readonly</b>] &lt;property-identifier&gt; ]
 * <p/>
 * <i>method-statement</i>
 *   &lt;function-definition&gt;
 * <p/>
 * <i>constructor-statement</i>
 *   [modifiers] <b>function</b> &lt;class-identifier&gt; ( [ &lt;argument-declaration-list&gt; ] ) &lt;statement-block&gt;
 * <p/>
 * <i>property-statement</i>
 *   [modifiers] <b>property</b> <b>get</b> &lt;property-identifier&gt; () : &lt;type-literal&gt; &lt;statement-block&gt;
 *   [modifiers] <b>property</b> <b>set</b> &lt;property-identifier&gt; ( [ &lt;argument-declaration-list&gt; ] ) &lt;statement-block&gt;
 * <p/>
 * <i>argument-declaration-list</i>
 *   &lt;argument-declaration&gt;
 *   &lt;argument-declarationlist&gt; , &lt;argument-declaration&gt;
 * <p/>
 * <i>argument-declaration</i>
 *   &lt;identifier&gt; : &lt;type-literal&gt;
 * <p/>
 * <i>modifiers</i>
 *   <b>private</b>
 *   <b>public</b>
 *   <b>override</b>
 *   <b>hide</b>
 * <p/>
 * <i>scope-attribute</i>
 *   <b>execution</b>
 *   <b>request</b>
 *   <b>session</b>
 *   <b>application</b>
 * <p/>
 * <i>try-catch-finally-statement</i>
 *   <b>try</b> &lt;statement&gt; [ <b>catch</b> <b>(</b> &lt;identifier&gt; <b>)</b> &lt;statement&gt; ] [ <b>finally</b> &lt;statement&gt; ]
 * <p/>
 * <i>throw-statement</i>
 *   <b>throw</b> &lt;expression&gt;
 * <p/>
 * </pre>
 *
 * @see IGosuParserFactory
 * 
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuParser extends IParserPart
{
  //
  // Stock types
  // NOTE: Users of this class should use these when assigning types to ISymbol instances
  //

  public IType NUMBER_TYPE = IJavaType.DOUBLE;
  public IType STRING_TYPE = IJavaType.STRING;
  public IType CHAR_TYPE = IJavaType.pCHAR;
  public IType BOOLEAN_TYPE = IJavaType.BOOLEAN;
  public IType DATETIME_TYPE = IJavaType.DATE;
  public IType LIST_TYPE = IJavaType.LIST;
  public IType NULL_TYPE = IJavaType.pVOID;
  public IType ARRAY_NUMBER_TYPE = NUMBER_TYPE.getArrayType();
  public IType ARRAY_STRING_TYPE = STRING_TYPE.getArrayType();
  public IType ARRAY_BOOLEAN_TYPE = BOOLEAN_TYPE.getArrayType();
  public IType ARRAY_DATETIME_TYPE = DATETIME_TYPE.getArrayType();
  public IType GENERIC_BEAN_TYPE = IJavaType.OBJECT;
  public IType GENERIC_ARRAY_TYPE = IJavaType.OBJECT.getArrayType();

  //
  // Reusable, cached objects
  //

  // Cache high-volume values
  public Double NaN = Double.NaN;
  public Double POS_INFINITY = Double.POSITIVE_INFINITY;
  public Double NEG_INFINITY = Double.NEGATIVE_INFINITY;
  public Double ZERO = (double)0;
  public Double ONE = (double)1;
  public Double[] DOUBLE_DIGITS = new Double[]{ZERO, ONE, 2d, 3d, 4d, 5d, 6d, 7d, 8d, 9d};
  public String EMPTYSTRING = "";
  public BigDecimal BIGD_ZERO = new BigDecimal( 0 );

  /**
   * The context associated with the parser's script. E.g., a file name, a library,
   * a rule, etc.
   */
  public IScriptPartId getScriptPart();

  /**
   * Set the script or expression to parse and execute.
   *
   * @param strRule The rule (Gosu) source to parse/execute.
   */
  public void setScript( CharSequence strRule );

  /**
   * Set the script or expression to parse and execute.
   *
   * @param reader A reader for the rule (Gosu) source to parse/execute.
   */
  public void setScript( SourceCodeReader reader );

  /**
   * Set the script or expression to parse and execute.
   *
   * @param reader A reader for the rule (Gosu) source to parse/execute.
   */
  public void setScript( ParserSource reader );

  /**
   * Returns the parser's symbol table.
   */
  public ISymbolTable getSymbolTable();

  /**
   * Sets the parser's symbol table.
   *
   * @param symTable The symbol table the parser will use when parsing and executing rules.
   */
  public void setSymbolTable( ISymbolTable symTable );

  /**
   * Get the type uses map.
   */
  public ITypeUsesMap getTypeUsesMap();

  /**
   * Set the type uses map.
   */
  public void setTypeUsesMap( ITypeUsesMap typeUsesMap );

  /**
   * Parses a set of Gosu statements.  To execute all of the Statements at once call Statement.execute().
   *
   * @return The parsed Statement[s].
   *
   * @throws ParseResultsException if any of the statements do not parse according to the Gosu grammar.
   */
  public IStatement parseStatements( IScriptPartId partId ) throws ParseResultsException;

  public IProgram parseProgram( IScriptPartId partId ) throws ParseResultsException;

  public IProgram parseProgram( IScriptPartId partId, IType expectedExpressionType ) throws ParseResultsException;

  public IProgram parseProgram( IScriptPartId partId, IType expectedExpressionType, IFileContext ctx, boolean assignRuntime ) throws ParseResultsException;

  public IProgram parseProgram( IScriptPartId partId, boolean isolatedScope, boolean reallyIsolatedScope, IType expectedExpressionType, IFileContext ctx, boolean assignRuntime ) throws ParseResultsException;  

  public IProgram parseProgram( IScriptPartId partId, boolean isolatedScope, boolean reallyIsolatedScope, IType expectedExpressionType, IFileContext ctx, boolean assignRuntime, IType superType ) throws ParseResultsException;  
  /**
   * For use by code editors etc.
   */
  public IGosuClass parseClass( String strQualifiedClassName, ISourceFileHandle sourceFile, boolean bThrowOnWarnings, boolean bFullyCompile ) throws ParseResultsException;

  /**
   * @return
   * @throws ParseResultsException
   */
  public ISymbol[] parseProgramFunctionsOrPropertyDecls(IScriptPartId partId, boolean bParseProperties, boolean bParseVars) throws ParseResultsException;

  /**
   * Parses a Gosu expression. To evaluate the Expression simply call Expression.evaluate().
   *
   * @return The parsed Expression.
   *
   * @throws ParseResultsException if the expression does not parse according to the Gosu grammar.
   */
  public IExpression parseExp( IScriptPartId partId ) throws ParseResultsException;


  /**
   * Parses a Gosu expression. To evaluate the Expression simply call Expression.evaluate().
   *
   * @return The parsed Expression.
   *
   * @throws ParseResultsException if the expression does not parse according to the Gosu grammar.
   */
  public IExpression parseExp( IScriptPartId partId, IType expectedExpressionType ) throws ParseResultsException;

  /**
   * Parses a Gosu expression. To evaluate the Expression simply call Expression.evaluate().
   *
   * @return The parsed Expression.
   *
   * @throws ParseResultsException if the expression does not parse according to the Gosu grammar.
   */
  public IExpression parseExp( IScriptPartId partId, IType expectedExpressionType, IFileContext context, boolean assignRuntime ) throws ParseResultsException;

  /**
   * Parses a Gosu expression.  If that fails, attempts to parse a Gosu program (which is also an expression, but
   * which has a different grammar.
   *
   * @param partId Script part id
   * @return either a pure expression or Program, depending on the source
   *
   * @throws ParseResultsException if neither an expression nor a program parses according to the Gosu grammar.  We
   *                               try to make a best guess as to which IParseResultsException to throw, so that the
   *                               errors are as close as possible to the true cause of the IParseResultsException
   */
  public IExpression parseExpOrProgram( IScriptPartId partId ) throws ParseResultsException;

  /**
   * Parses a Gosu expression.  If that fails, attempts to parse a Gosu program (which is also an expression, but
   * which has a different grammar.
   *
   * @param partId Script part id
   * @param isolatedScope if false, the program will modify the symbol table at the current scope
   * @return either a pure expression or Program, depending on the source
   *
   * @throws ParseResultsException if neither an expression nor a program parses according to the Gosu grammar.  We
   *                               try to make a best guess as to which IParseResultsException to throw, so that the
   *                               errors are as close as possible to the true cause of the IParseResultsException
   */
  public IExpression parseExpOrProgram( IScriptPartId partId, boolean isolatedScope, boolean assignRuntime ) throws ParseResultsException;

  /**
   * Parses a type literal expression.  The source must obviously satisfy the type literal syntax.
   */
  ITypeLiteralExpression parseTypeLiteral( IScriptPartId partId ) throws ParseResultsException;

  /**
   * Consumes a type literal from the current tokenizer, if one exists.
   *
   * @return true if a type literal was found, false otherwise
   */
  boolean parseTypeLiteral();

  /**
   * @return Whether or not the referenced Gosu source has been parsed.
   */
  public boolean isParsed();

  /**
   * @return Did the most recent parse have warnings
   */
  public boolean hasWarnings();

  /**
   * @return All the locations corresponding to parsed elements.
   */
  public List<IParseTree> getLocations();

  /**
   * The TokenizerInstructor to use for this parser. Optional.
   */
  public ITokenizerInstructor getTokenizerInstructor();

  public void setTokenizerInstructor( ITokenizerInstructor instructor );

  public IExpression parseAsClassExpression( IGosuClass expressionOwner, IGosuParser.ParseType parseAsExpression ) throws ParseResultsException;

  public void setOffset( int sourceOffset, boolean sourceIsPartial );

  public Map<String, ITypeVariableDefinition> getTypeVariables();

  public boolean isThrowParseResultsExceptionForWarnings();

  public void setThrowParseExceptionForWarnings( boolean bThrowParseExceptionForWarnings );

  void setDontOptimizeStatementLists( boolean b );

  void setWarnOnCaseIssue( boolean warnOnCaseIssue );

  void setEditorParser( boolean bEditorParser );

  boolean isEditorParser();

  void putDfsDeclsInTable( ISymbolTable table );

  ISourceCodeTokenizer getTokenizer();

  ArrayList<ISymbol> parseParameterDeclarationList( IParsedElement pe, boolean bStatic, List<IType> inferredArgumentTypes );

  void putDfsDeclInSetByName( IDynamicFunctionSymbol specialFunction );

  ITypeLiteralExpression resolveTypeLiteral( String strName );

  Map<CaseInsensitiveCharSequence, Set<IFunctionSymbol>> getDfsDecls();

  public IParserState getState();

  boolean isCaptureSymbolsForEval();

  void setCaptureSymbolsForEval( boolean bCaputreSymbolsForEval );

  void setDfsDeclInSetByName( Map<CaseInsensitiveCharSequence, Set<IFunctionSymbol>> dfsDecl );

  boolean isParsingFunction();

  boolean isParsingBlock();

  IProgram parseProgram( IScriptPartId partId, boolean isolatedScope, IType expectedExpressionType ) throws ParseResultsException;

  void setGenerateRootExpressionAccessForProgram( boolean bGenRootExprAccess );
  
  void snapshotSymbolTableAt(int position);
  
  ISymbolTable getSnapshotSymbols();

  IUsesStatementList parseUsesStatementList( boolean resolveTypes );

  IExpression popExpression();

  String parseDotPathWord();

  void setTokenizer( ISourceCodeTokenizer tokenizer );

  enum ParseType
  {
    EXPRESSION,
    PROGRAM,
    CLASS_FRAGMENT,
    EXPRESSION_OR_PROGRAM,
  }

  class Settings
  {
    // Thread local to allow a thread (e.g. bulk fix thread) the ability to turn on case warnings for all
    // Gosu parsers in its thread
    public static ThreadLocal<Boolean> WARN_ON_CASE_DEFAULT = new ThreadLocal<Boolean>();

    // Thread local to allow a thread (e.g. bulk fix thread) the ability to retain parse tree info for warnings
    public static ThreadLocal<Boolean> IDE_EDITOR_PARSER_DEFAULT = new ThreadLocal<Boolean>();

    public static boolean STANDARDIZE_LINE_ENDINGS = false;

    public static boolean isAutoBoxedMatch( IType classIntrinsicType, IType argIntrinsicType )
    {
      return TypeSystem.isBoxedTypeFor( classIntrinsicType, argIntrinsicType ) ||
             TypeSystem.isBoxedTypeFor( argIntrinsicType, classIntrinsicType );
    }

    public static void setWarnOnCaseIssue()
    {
      WARN_ON_CASE_DEFAULT.set( Boolean.TRUE );
    }

    public static void clearWarnOnCaseIssue()
    {
      WARN_ON_CASE_DEFAULT.set( Boolean.FALSE );
    }

    public static void setIdeEditorParserDefault()
    {
      IDE_EDITOR_PARSER_DEFAULT.set( Boolean.TRUE );
    }

    public static void clearIdeEditorParserDefault()
    {
      IDE_EDITOR_PARSER_DEFAULT.set( Boolean.FALSE );
    }

    public static String standardizeSource( String content )
    {
      if(STANDARDIZE_LINE_ENDINGS) {
      return content.replaceAll( "\\r\\n", "\n" );
      } else {
        return content;
      }
    }

    public static String highlightCurrentToken( int start, int end, String source )
    {
      StringBuilder sb = new StringBuilder();
      sb.append( source.substring( 0, start ) );
      sb.append( ">>" );
      sb.append( source.substring( start, end ) );
      sb.append( "<<" );
      sb.append( source.substring( end, source.length() ) );
      return sb.toString();
    }
  }

}
