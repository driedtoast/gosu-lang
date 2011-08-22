package gw.lang.ir.builder;

import gw.lang.ir.IRType;
import gw.lang.GosuShop;
import gw.lang.UnstableAPI;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaClassInfo;

import java.util.List;
import java.util.ArrayList;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public abstract class IRBuilder {

  // ------------------------ Protected Helper Methods

  protected final IRType getIRType( Class cls ) {
    return GosuShop.getIRTypeResolver().getDescriptor( cls );
  }

  protected final IRType getIRType( IType type ) {
    return GosuShop.getIRTypeResolver().getDescriptor( type );
  }

  protected final IRType getIRType( IJavaClassInfo cls ) {
    return GosuShop.getIRTypeResolver().getDescriptor( cls );
  }

  protected final List<IRType> getIRTypes( Class[] classes ) {
    List<IRType> results = new ArrayList<IRType>();
    for (Class cls : classes) {
      results.add( getIRType( cls ) );
    }
    return results;
  }
}
