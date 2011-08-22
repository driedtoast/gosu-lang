package gw.lang.reflect;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.IExpression;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IErrorType extends INonLoadableType
{
  String NAME = "ErrorType";

  String getErrantTypeName();

  ParseResultsException getError();

  IFunctionType getErrorTypeFunctionType( IExpression[] eArgs, String strMethod, List listAllMatchingMethods );

  IConstructorType getErrorTypeConstructorType( IExpression[] eArgs, List listAllMatchingMethods );
}
