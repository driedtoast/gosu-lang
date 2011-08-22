package gw.lang.javadoc;

import gw.lang.UnstableAPI;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface JavaHasParams {

  public IDocRef<IParamNode> getDocsForParam( int paramIndex );

}
