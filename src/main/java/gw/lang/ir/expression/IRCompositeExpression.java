package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a composite expression.  A composite expression consists of 0 or more statements
 * followed by a single expression.  The type of that final expression is also the type of the overall
 * composite expression, and that last expression's result represents the result of the overall expression.
 *
 * Composite expressions are used to represent things that look like expressions from the point of the language
 * but which do not map to a single expression in bytecode.  For example, creating an initialized array, i.e.
 * <code>new String[]{"foo", "bar"}</code> results in an expression in Java or in Gosu, but actually needs
 * to be compiled as a composite expression consisting of an assignment of a new array to a temp variable,
 * assignments into that array, and lastly an identifier expression that leaves the initialized array on the
 * top of the stack:
 *
 * <code>var temp = new String[2]</code>
 * <code>temp[0] = "foo"</code>
 * <code>temp[1] = "bar"</code>
 * <code>temp</code> 
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRCompositeExpression extends IRExpression {
  private List<IRElement> _elements;

  public IRCompositeExpression(IRElement... elements) {
    _elements = new ArrayList<IRElement>(Arrays.asList(elements));

    for(IRElement element : _elements) {
      element.setParent(this);
    }
  }

  public IRCompositeExpression(List<IRElement> elements) {
    _elements = elements;

    for(IRElement element : _elements) {
      element.setParent(this);
    }
  }

  public List<IRElement> getElements() {
    return _elements;
  }

  public void addElement(IRElement element) {
    _elements.add(element);
    element.setParent( this );
  }

  @Override
  public IRType getType() {
    return ((IRExpression) _elements.get(_elements.size() - 1)).getType();
  }
}
