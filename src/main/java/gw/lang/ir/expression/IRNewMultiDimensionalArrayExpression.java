package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a new multidimensional array, i.e. <code>new String[10][10]</code>.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRNewMultiDimensionalArrayExpression extends IRExpression {
  private IRType _resultType;
  private List<IRExpression> _sizeExpressions;

  public IRNewMultiDimensionalArrayExpression(IRType resultType, List<IRExpression> sizeExpressions) {
    _resultType = resultType;
    _sizeExpressions = new ArrayList<IRExpression>(sizeExpressions);

    for (IRExpression arg : sizeExpressions) {
      arg.setParent( this );
    }
  }

  public IRType getResultType() {
    return _resultType;
  }

  public List<IRExpression> getSizeExpressions() {
    return _sizeExpressions;
  }

  @Override
  public IRType getType() {
    return _resultType;
  }
}
