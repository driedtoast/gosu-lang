package gw.lang.parser;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ILanguageLevel
{
  boolean allowNonLiteralArgsForJavaAnnotations();

  boolean allowNumericIteration();

  boolean allowGlobalNowSymbol();

  boolean allowAllImplicitCoercions();

  boolean allowImplicitBigNumbersWithinExpressions();

  boolean isStandard();

  boolean errorOnStringCoercionInAdditiveRhs();

  boolean richNPEsInMathematicalExpressions();

  boolean supportsNakedCatchStatements();

  boolean allowsFeatureLiterals();

  boolean shouldVerifyPackageRelativeImport( String parsedNameSpace, String actualNameSpace );

  boolean allowPackageRelativeImports();
}
