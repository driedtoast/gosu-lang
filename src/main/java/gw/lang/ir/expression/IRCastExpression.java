package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 * IR expression representing a cast expression, i.e. <code>(String) foo</code>
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRCastExpression extends IRExpression {
  private IRExpression _root;
  private IRType _type;

  public IRCastExpression(IRExpression root, IRType type) {
    if (type == null) {
      throw new IllegalArgumentException("The type to cast to cannot be null");
    }
    if (type.isPrimitive()) {
      throw new IllegalArgumentException("Cannot perform a cast to a primitive type: " + type.getName());
    }

    //## hack: Don't ever cast to an external entity type
    type = isExternalEntityType( type ) ? root.getType() : type;

    _root = root;
    _type = type;

    root.setParent( this );
  }

  private boolean isExternalEntityType( IRType type )
  {
    String prefix = "com.guidewire.";
    if( type.getName().startsWith( prefix ) )
    {
      return type.getName().substring( prefix.length() + 2 ).startsWith( ".external." );
    }
    return false;
  }

  public IRExpression getRoot() {
    return _root;
  }

  public IRType getType() {
    return _type;
  }
}
