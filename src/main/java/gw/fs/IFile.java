package gw.fs;

import gw.lang.UnstableAPI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * NOTE: This API is in flux.  This class may be moved to a different package, renamed, or have its methods change
 * in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IFile extends IResource {
  InputStream openInputStream() throws IOException;

  OutputStream openOutputStream() throws IOException;

  OutputStream openOutputStreamForAppend() throws IOException;

  String getExtension();

  String getBaseName();
}
