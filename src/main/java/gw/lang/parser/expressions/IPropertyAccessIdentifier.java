

package gw.lang.parser.expressions;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IPropertyAccessIdentifier extends IIdentifierExpression {

  IIdentifierExpression getWrappedIdentifier();

}