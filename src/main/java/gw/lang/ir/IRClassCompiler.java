package gw.lang.ir;

import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IRClassCompiler {
  byte[] compile( IRClass irClass, boolean debug );

  String compileToJava( IRClass irClass );
}
