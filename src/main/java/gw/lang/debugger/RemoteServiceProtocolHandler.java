package gw.lang.debugger;

/**
 * @Deprecated The debugger API is obsolete now that Gosu compiles directly to bytecode
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface RemoteServiceProtocolHandler
{
  public String call( String strSessionId, String strService, String strMethod, String strParamTypes, String strArgs ) throws Exception;
}
