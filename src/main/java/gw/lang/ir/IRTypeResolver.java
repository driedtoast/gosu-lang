package gw.lang.ir;

import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IRTypeResolver {

  IRType getDescriptor( IType type );

  IRType getDescriptor( Class cls );

  IRType getDescriptor( IJavaClassInfo cls );
}
