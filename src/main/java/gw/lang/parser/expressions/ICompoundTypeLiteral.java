package gw.lang.parser.expressions;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ICompoundTypeLiteral
{
  List<? extends ITypeLiteralExpression> getTypes();
  void setTypes( List<? extends ITypeLiteralExpression> types );
}
