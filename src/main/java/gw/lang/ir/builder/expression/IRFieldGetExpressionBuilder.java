package gw.lang.ir.builder.expression;

import gw.lang.UnstableAPI;
import gw.lang.ir.IJavaClassIRType;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.expression.IRFieldGetExpression;
import gw.lang.ir.statement.IRFieldDecl;

import java.lang.reflect.Field;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRFieldGetExpressionBuilder extends IRExpressionBuilder {

  private BuilderImpl _builder;

  public IRFieldGetExpressionBuilder(IRExpressionBuilder root, String name) {
    _builder = new RootAndNameBuilder(root, name);
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return _builder.buildImpl(context);
  }

  private static interface BuilderImpl {
    IRFieldGetExpression buildImpl(IRBuilderContext context);
  }

  private static final class RootAndNameBuilder implements BuilderImpl {
    private IRExpressionBuilder _root;
    private String _name;

    private RootAndNameBuilder(IRExpressionBuilder root, String name) {
      _root = root;
      _name = name;
    }

    @Override
    public IRFieldGetExpression buildImpl(IRBuilderContext context) {
      IRExpression root = _root.build(context);
      IRType rootType = root.getType();
      if ( rootType instanceof IJavaClassIRType) {
        Field field = findField( rootType.getJavaClass(), _name );
        return new IRFieldGetExpression(root, _name, getIRType(field.getType()), getIRType(field.getDeclaringClass()));
      } else if ( rootType.equals( context.owningType() ) ) {
        IRFieldDecl field = context.findField(_name);
        return new IRFieldGetExpression(root, _name, field.getType(), context.owningType() );
      } else {
        throw new IllegalArgumentException("Cannot reference a field only by name on a root expression that's not an IJavaClassIRType");
      }
    }
  }
}
