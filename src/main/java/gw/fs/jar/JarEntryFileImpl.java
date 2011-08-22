package gw.fs.jar;

import gw.lang.UnstableAPI;
import gw.fs.IFile;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class JarEntryFileImpl extends JarEntryResourceImpl implements IFile {

  public JarEntryFileImpl(String name, IJarFileDirectory parent, JarFileDirectoryImpl jarFile) {
    super(name, parent, jarFile);
  }

  @Override
  public InputStream openInputStream() throws IOException {
    if (_entry == null) {
      throw new IOException();
    }
    return _jarFile.getInputStream(_entry);
  }

  @Override
  public OutputStream openOutputStream() throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public OutputStream openOutputStreamForAppend() throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getExtension() {
    int lastDot = _name.lastIndexOf(".");
    if (lastDot != -1) {
      return _name.substring(lastDot + 1);
    } else {
      return "";
    }
  }

  @Override
  public String getBaseName() {
    int lastDot = _name.lastIndexOf(".");
    if (lastDot != -1) {
      return _name.substring(0, lastDot);
    } else {
      return _name;
    }
  }
}
