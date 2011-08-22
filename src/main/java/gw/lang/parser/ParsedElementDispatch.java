

package gw.lang.parser;

import gw.lang.parser.expressions.*;
import gw.lang.parser.statements.IArrayAssignmentStatement;
import gw.lang.parser.statements.IAssignmentStatement;
import gw.lang.parser.statements.IBeanMethodCallStatement;
import gw.lang.parser.statements.IBreakStatement;
import gw.lang.parser.statements.ICatchClause;
import gw.lang.parser.statements.IClassDeclaration;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.IClasspathStatement;
import gw.lang.parser.statements.IContinueStatement;
import gw.lang.parser.statements.IDoWhileStatement;
import gw.lang.parser.statements.IEvalStatement;
import gw.lang.parser.statements.IForEachStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.IIfStatement;
import gw.lang.parser.statements.IMapAssignmentStatement;
import gw.lang.parser.statements.IMemberAssignmentStatement;
import gw.lang.parser.statements.IMethodCallStatement;
import gw.lang.parser.statements.INamespaceStatement;
import gw.lang.parser.statements.INoOpStatement;
import gw.lang.parser.statements.INotAStatement;
import gw.lang.parser.statements.IPropertyStatement;
import gw.lang.parser.statements.IReturnStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.parser.statements.ISwitchStatement;
import gw.lang.parser.statements.ISyntheticFunctionStatement;
import gw.lang.parser.statements.IThrowStatement;
import gw.lang.parser.statements.ITryCatchFinallyStatement;
import gw.lang.parser.statements.ITypeLoaderStatement;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.parser.statements.IUsesStatementList;
import gw.lang.parser.statements.IUsingStatement;
import gw.lang.parser.statements.IWhileStatement;
import gw.lang.parser.statements.ISyntheticMemberAccessStatement;
import gw.lang.parser.statements.IBlockInvocationStatement;


/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@SuppressWarnings({"UnusedDeclaration"})
public abstract class ParsedElementDispatch<R> {

  public R dispatch(IParsedElementWithAtLeastOneDeclaration pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IAdditiveExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IMultiplicativeExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IBitwiseOrExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IBitwiseXorExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IBitwiseAndExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IBitshiftExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IIntervalExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IBlockExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IBlockInvocation pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch( IBlockInvocationStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IArrayAccessExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IConditionalAndExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IConditionalOrExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IEqualityExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IRelationalExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IWhereClauseConditionalAndExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IWhereClauseConditionalOrExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IWhereClauseEqualityExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IWhereClauseRelationalExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IConditionalTernaryExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IEvalExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IExistsExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IIdentifierExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IPropertyAccessIdentifier pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IInitializerExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IBooleanLiteralExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(INumericLiteralExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(ICharLiteralExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IStringLiteralExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(ITypeLiteralExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IFeatureLiteralExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IParenthesizedExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IBlockLiteralExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IMapAccessExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IBeanMethodCallExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IFieldAccessExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(ISynthesizedMemberAccessExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IMethodCallExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(INewExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IInferredNewExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(INullExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IObjectLiteralExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch( ILocalVarDeclaration pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IProgram pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IQueryExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IQueryPathExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IStaticTypeOfExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(ITypeAsExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IImplicitTypeAsExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(ITypeIsExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(ITypeOfExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IUnaryExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IUnaryNotPlusMinusExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IWhereClauseUnaryExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IClassDeclaration pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IForEachStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IFunctionStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IPropertyStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(ITryCatchFinallyStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(ICatchClause pe) {
    throw new NoDispatchDefinedException();
  }
  
  public R dispatch( ITypeVariableDefinitionExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IVarStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IArrayAssignmentStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IAssignmentStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IBeanMethodCallStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(ISyntheticMemberAccessStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IClassFileStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IClassStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IIfStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IUsingStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IDoWhileStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IWhileStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IMapAssignmentStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IMemberAssignmentStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IMethodCallStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch( IEvalStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(INamespaceStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(INoOpStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch( ISyntheticFunctionStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(INotAStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IStatementList pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(ISwitchStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IIdentityExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IBreakStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IContinueStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IReturnStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IThrowStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IUsesStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IUsesStatementList pe) {
    throw new NoDispatchDefinedException();
  }
  
  public R dispatch(IClasspathStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch( ITypeLoaderStatement pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IDirectiveExpression directiveExpression) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(ITemplateStringLiteral pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch(IObjectInitializerExpression pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch( IInitializerAssignment pe) {
    throw new NoDispatchDefinedException();
  }

  public R dispatch( IListLiteralExpression le) {
    throw new NoDispatchDefinedException();
  }

  public static class NoDispatchDefinedException extends UnsupportedOperationException {
    @Override
    public synchronized Throwable fillInStackTrace() {
      return this;
    }
  }

}
