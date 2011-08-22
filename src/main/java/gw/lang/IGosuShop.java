package gw.lang;

import gw.config.IService;
import gw.fs.IDirectory;
import gw.lang.init.GosuPathEntry;
import gw.lang.ir.IRClassCompiler;
import gw.lang.ir.IRTypeResolver;
import gw.lang.javadoc.IJavaDocFactory;
import gw.lang.parser.IConstructorInfoFactory;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.IFullParserState;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IParserPart;
import gw.lang.parser.IScope;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.IStack;
import gw.lang.parser.IStackProvider;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.INullExpression;
import gw.lang.parser.template.ITemplateGenerator;
import gw.lang.parser.template.ITemplateHost;
import gw.lang.parser.template.TemplateParseException;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IAnnotationInfoFactory;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IEntityAccess;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeInfoFactory;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.GosuTemplateTypeLoader;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IEnhancementIndex;
import gw.lang.reflect.gs.IFileSystemGosuClassRepository;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuClassLoader;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.gs.ITemplateType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IClassPath;
import gw.fs.IFile;
import gw.lang.reflect.module.IModule;
import gw.lang.reflect.module.ModuleClassLoader;
import gw.lang.tidb.IFeatureInfoRecordFactory;
import gw.util.GosuExceptionUtil;
import gw.util.IFeatureFilter;
import gw.util.IGosuEditor;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Serves as an intermediary between the Gosu API module and the Gosu internal module.
 * @see GosuShop
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuShop extends IService
{
  ISymbolTable createSymbolTable();
  ISymbolTable createSymbolTable( boolean bDefineCommonSymbols );

  ITemplateHost createTemplateHost();

  IConstructorInfoFactory getConstructorInfoFactory();
  IAnnotationInfoFactory getAnnotationInfoFactory();
  IFeatureInfoRecordFactory getFeatureInfoRecordFactory();
  IJavaDocFactory getJavaDocFactory();

  IPropertyInfo createLengthProperty(ITypeInfo typeInfo);

  IFunctionType createFunctionType( IMethodInfo mi );

  IDynamicFunctionSymbol createFunctionFromInterface( IType typeToCoerceTo, Object value );

  ISymbol createSymbol( CharSequence name, IType type, Object value );
  ISymbol createSymbol( CharSequence name, IType type, IStackProvider stackProvider );

  IClassPath getClassPath();

  ITypeInfoFactory getTypeInfoFactory();

  IEntityAccess getDefaultEntityAccess();

  ITemplateHost createSimpleTemplateHost();

  ISourceCodeTokenizer createSourceCodeTokenizer( CharSequence code );
  ISourceCodeTokenizer createSourceCodeTokenizer( CharSequence code, boolean bTemplate );  
  ISourceCodeTokenizer createSourceCodeTokenizer( Reader reader );

  IScope createCommnoSymbolScope();

  IStack createGosuStack();

  IIdentifierExpression createIdentifierExpression();

  void generateTemplate( Reader readerTemplate, Writer writerOut, ISymbolTable symbolTable ) throws TemplateParseException;

  ISymbolTable getGosuClassSymbolTable();

  ISymbol createDynamicFunctionSymbol( ISymbolTable symbolTable, String strMemberName, IFunctionType functionType, List<ISymbol> params, IExpression value );

  IEnhancementIndex createEnhancementIndex( GosuClassTypeLoader loader );

  IGosuClass createClass( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap );
  IGosuProgram createProgram( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap, ISymbolTable symTable );  
  IGosuProgram createProgramForEval( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap, ISymbolTable symTable );
  IGosuEnhancement createEnhancement( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap );
  ITemplateType createTemplate( String strFqn, ITemplateGenerator template, GosuTemplateTypeLoader loader );
  ICompilableType findFragment( String fullyQualifiedName );
  void clearFragments();

  IFileSystemGosuClassRepository createFileSystemGosuClassRepository(IDirectory[] files, boolean includeCoreResources);
  IFileSystemGosuClassRepository createFileSystemGosuClassRepository(IDirectory[] files, String[] extensions, boolean includeCoreResources);

  ITypeUsesMap createTypeUsesMap( List<String> specialTypeUses );

  IPropertyInfo getPropertyInfo( IType classBean, String strProperty, IFeatureFilter filter, IParserPart parserBase, IScriptabilityModifier scriptabilityConstraint) throws ParseException;
  List<? extends IPropertyInfo> getProperties( ITypeInfo beanInfo, IType classSource );
  boolean isDescriptorHidden( IAttributedFeatureInfo pi );
  List<? extends IMethodInfo> getMethods( ITypeInfo beanInfo, IType ownersIntrinsicType );

  IFullParserState createStandardParserState( IParsedElement rootParsedElement, String scriptSrc, boolean b );

  RuntimeException createEvaluationException(String msg);

  IPropertyInfo createPropertyDelegate(IFeatureInfo container, IPropertyInfo prop);

  IModule createModule( String strMemberName );

  INullExpression getNullExpressionInstance();
  
  IGosuClassLoader createGosuClassLoader( ModuleClassLoader moduleClassLoader );

  GosuExceptionUtil.IForceThrower getForceThrower();

  Class getBlockToInterfaceConversionClass( IJavaType typeToCoerceTo );

  IRTypeResolver getIRTypeResolver();

  IRClassCompiler getIRClassCompiler();
  
  Object evaluateAnnotation( IAnnotationInfo ai );

  IPropertyAccessor getLengthAccessor();

  GosuPathEntry createPathEntryFromModuleFile(IFile f);

  IGosuEditor createGosuEditor();

  Method[] getDeclaredMethods( Class cls );

  boolean isAnnotationAllowedMultipleTimes( IFeatureInfo fi, IAnnotationInfo annotationInfo );
}
