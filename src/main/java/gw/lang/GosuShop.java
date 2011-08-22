package gw.lang;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.init.GosuPathEntry;
import gw.lang.ir.IRClassCompiler;
import gw.lang.ir.IRTypeResolver;
import gw.lang.javadoc.IJavaDocFactory;
import gw.lang.parser.IConstructorInfoFactory;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.IFullParserState;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IScope;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.IStack;
import gw.lang.parser.IStackProvider;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.INullExpression;
import gw.lang.parser.template.ITemplateGenerator;
import gw.lang.parser.template.ITemplateHost;
import gw.lang.parser.template.TemplateParseException;
import gw.lang.reflect.IAnnotationInfoFactory;
import gw.lang.reflect.IEntityAccess;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
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
import gw.lang.reflect.module.IModule;
import gw.lang.reflect.module.ModuleClassLoader;
import gw.lang.tidb.IFeatureInfoRecordFactory;

import java.io.Reader;
import java.io.Writer;
import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class GosuShop
{
  public static ISymbolTable createSymbolTable()
  {
    return CommonServices.getGosuIndustrialPark().createSymbolTable();
  }

  public static ISymbolTable createSymbolTable( boolean bDefineCommonSymbols )
  {
    return CommonServices.getGosuIndustrialPark().createSymbolTable( bDefineCommonSymbols );
  }

  public static ITemplateHost createTemplateHost()
  {
    return CommonServices.getGosuIndustrialPark().createTemplateHost();
  }

  public static IPropertyInfo createLengthProperty(ITypeInfo typeInfo)
  {
    return CommonServices.getGosuIndustrialPark().createLengthProperty(typeInfo);
  }

  public static IFunctionType createFunctionType( IMethodInfo mi )
  {
    return CommonServices.getGosuIndustrialPark().createFunctionType( mi );
  }

  public static IDynamicFunctionSymbol createFunctionFromInterface( IType typeToCoerceTo, Object value )
  {
    return CommonServices.getGosuIndustrialPark().createFunctionFromInterface( typeToCoerceTo, value );
  }

  public static ITypeInfoFactory getTypeInfoFactory()
  {
    return CommonServices.getGosuIndustrialPark().getTypeInfoFactory();
  }

  public static IConstructorInfoFactory getConstructorInfoFactory()
  {
    return CommonServices.getGosuIndustrialPark().getConstructorInfoFactory();
  }

  public static IAnnotationInfoFactory getAnnotationInfoFactory()
  {
    return CommonServices.getGosuIndustrialPark().getAnnotationInfoFactory();
  }

  public static IJavaDocFactory getJavaDocFactory()
  {
    return CommonServices.getGosuIndustrialPark().getJavaDocFactory();
  }

  public static IFeatureInfoRecordFactory getFeatureInfoRecordFactory()
  {
    return CommonServices.getGosuIndustrialPark().getFeatureInfoRecordFactory();
  }

  public static ISymbol createSymbol( CharSequence name, IType type, Object value )
  {
    return CommonServices.getGosuIndustrialPark().createSymbol( name, type, value );
  }
  public static ISymbol createSymbol( CharSequence name, IType type, IStackProvider stackProvider )
  {
    return CommonServices.getGosuIndustrialPark().createSymbol( name, type, stackProvider );
  }
  
  public static IClassPath getClassPath()
  {
    return CommonServices.getGosuIndustrialPark().getClassPath();
  }

  public static IEntityAccess getDefaultEntityAccess()
  {
    return CommonServices.getGosuIndustrialPark().getDefaultEntityAccess();
  }

  public static ITemplateHost createSimpleTemplateHost()
  {
    return CommonServices.getGosuIndustrialPark().createSimpleTemplateHost();
  }

  public static ISourceCodeTokenizer createSourceCodeTokenizer( CharSequence code )
  {
    return CommonServices.getGosuIndustrialPark().createSourceCodeTokenizer( code );
  }
  public static ISourceCodeTokenizer createSourceCodeTokenizer( CharSequence code, boolean bTemplate )
  {
    return CommonServices.getGosuIndustrialPark().createSourceCodeTokenizer( code, bTemplate );
  }
  public static ISourceCodeTokenizer createSourceCodeTokenizer( Reader reader )
  {
    return CommonServices.getGosuIndustrialPark().createSourceCodeTokenizer( reader );
  }

  public static IScope createCommonSymbolScope()
  {
    return CommonServices.getGosuIndustrialPark().createCommnoSymbolScope();
  }

  public static IStack createGosuStack()
  {
    return CommonServices.getGosuIndustrialPark().createGosuStack();
  }

  public static IIdentifierExpression createIdentifierExpression()
  {
    return CommonServices.getGosuIndustrialPark().createIdentifierExpression();
  }

  public static void generateTemplate( Reader readerTemplate, Writer writerOut, ISymbolTable threadLocalSymbolTable ) throws TemplateParseException
  {
    CommonServices.getGosuIndustrialPark().generateTemplate( readerTemplate, writerOut, threadLocalSymbolTable );
  }

  public static ISymbolTable getGosuClassSymbolTable()
  {
    return CommonServices.getGosuIndustrialPark().getGosuClassSymbolTable();
  }

  public static ISymbol createDynamicFunctionSymbol( ISymbolTable symbolTable, String strMemberName, IFunctionType functionType, List<ISymbol> params, IExpression expression )
  {
    return CommonServices.getGosuIndustrialPark().createDynamicFunctionSymbol( symbolTable, strMemberName, functionType, params, expression );
  }

  public static IEnhancementIndex createEnhancementIndex( GosuClassTypeLoader loader )
  {
    return CommonServices.getGosuIndustrialPark().createEnhancementIndex( loader );
  }

  public static IGosuClass createClass( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap )
  {
    return CommonServices.getGosuIndustrialPark().createClass( strNamespace, strRelativeName, loader, sourceFile, typeUsesMap );
  }
  public static IGosuProgram createProgram( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap, ISymbolTable symTable )
  {
    return CommonServices.getGosuIndustrialPark().createProgram( strNamespace, strRelativeName, loader, sourceFile, typeUsesMap, symTable );
  }
  public static IGosuProgram createProgramForEval( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap, ISymbolTable symTable )
  {
    return CommonServices.getGosuIndustrialPark().createProgramForEval( strNamespace, strRelativeName, loader, sourceFile, typeUsesMap, symTable );
  }
  public static IGosuEnhancement createEnhancement( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap )
  {
    return CommonServices.getGosuIndustrialPark().createEnhancement( strNamespace, strRelativeName, loader, sourceFile, typeUsesMap );
  }
  public static ITemplateType createTemplate( String strFqn, ITemplateGenerator template, GosuTemplateTypeLoader loader )
  {
    return CommonServices.getGosuIndustrialPark().createTemplate( strFqn, template, loader );
  }
  public static ICompilableType findFragment( String fullyQualfiedName ) {
    return CommonServices.getGosuIndustrialPark().findFragment( fullyQualfiedName );  
  }
  public static void clearFragments( ) {
    CommonServices.getGosuIndustrialPark().clearFragments();
  }

  public static IFileSystemGosuClassRepository createFileSystemGosuClassRepository(IDirectory[] files, boolean includeCoreResources)
  {
    return CommonServices.getGosuIndustrialPark().createFileSystemGosuClassRepository( files, includeCoreResources);
  }
  public static IFileSystemGosuClassRepository createFileSystemGosuClassRepository(IDirectory[] files, String[] extensions, boolean includeCoreResources)
  {
    return CommonServices.getGosuIndustrialPark().createFileSystemGosuClassRepository( files, extensions, includeCoreResources);
  }

  public static ITypeUsesMap createTypeUsesMap( List<String> specialTypeUses )
  {
    return CommonServices.getGosuIndustrialPark().createTypeUsesMap( specialTypeUses );
  }

  public static IFullParserState createStandardParserState( IParsedElement rootParsedElement, String scriptSrc, boolean b )
  {
    return CommonServices.getGosuIndustrialPark().createStandardParserState( rootParsedElement, scriptSrc, b );
  }

  public static RuntimeException createEvaluationException(String msg) {
    return CommonServices.getGosuIndustrialPark().createEvaluationException(msg);
  }

  public static IPropertyInfo createPropertyDelegate(IFeatureInfo container, IPropertyInfo prop) {
    return CommonServices.getGosuIndustrialPark().createPropertyDelegate(container, prop);
  }

  public static IModule createModule( String strMemberName )
  {
    return CommonServices.getGosuIndustrialPark().createModule( strMemberName );
  }

  public static INullExpression getNullExpressionInstance() {
    return CommonServices.getGosuIndustrialPark().getNullExpressionInstance();
  }
  
  public static IGosuClassLoader createGosuClassLoader( ModuleClassLoader moduleClassLoader )
  {
    return CommonServices.getGosuIndustrialPark().createGosuClassLoader( moduleClassLoader );
  }

  public static Class getBlockToInterfaceConversionClass( IJavaType typeToCoerceTo ) {
    return CommonServices.getGosuIndustrialPark().getBlockToInterfaceConversionClass( typeToCoerceTo );
  }

  public static IRTypeResolver getIRTypeResolver() {
    return CommonServices.getGosuIndustrialPark().getIRTypeResolver();
  }

  public static IRClassCompiler getIRClassCompiler() {
    return CommonServices.getGosuIndustrialPark().getIRClassCompiler();
  }

  public static IPropertyAccessor getLengthAccessor()
  {
    return CommonServices.getGosuIndustrialPark().getLengthAccessor();
  }

  public static GosuPathEntry createPathEntryFromModuleFile(IFile f) {
    return CommonServices.getGosuIndustrialPark().createPathEntryFromModuleFile(f);
  }
}
