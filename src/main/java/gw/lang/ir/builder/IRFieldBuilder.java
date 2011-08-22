package gw.lang.ir.builder;

import gw.lang.ir.IRType;
import gw.lang.ir.statement.IRFieldDecl;
import gw.lang.reflect.IType;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRFieldBuilder extends IRFeatureBuilder<IRFieldBuilder> {

  private IRClassBuilder _classBuilder;
  private String _name;
  private IRType _type;

  public IRFieldBuilder(IRClassBuilder classBuilder) {
    _classBuilder = classBuilder;
  }

  public IRFieldBuilder withName( String name ) {
    _name = name;
    return this;
  }

  public IRFieldBuilder withType( Class type ) {
    return withType( getIRType( type ) );
  }

  public IRFieldBuilder withType( IType type ) {
    return withType( getIRType( type ) );
  }

  public IRFieldBuilder withType( IRType type ) {
    _type = type;
    return this;
  }

  public IRFieldDecl build() {
    IRFieldDecl fieldDecl = new IRFieldDecl( _modifiers, _name, _type, null );
    _classBuilder.withField( fieldDecl );
    return fieldDecl;
  }
}