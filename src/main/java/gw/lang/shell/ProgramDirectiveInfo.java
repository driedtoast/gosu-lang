package gw.lang.shell;

import java.util.List;
import java.util.Collections;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ProgramDirectiveInfo
{
  private List<String> _classpaths;
  private List<String> _typeLoaders;

  public ProgramDirectiveInfo()
  {
    _classpaths = Collections.emptyList();
    _typeLoaders = Collections.emptyList();
  }

  List<String> getClasspaths()
  {
    return _classpaths;
  }

  List<String> getTypeLoaders()
  {
    return _typeLoaders;
  }

  public void setClasspaths( List<String> paths )
  {
    _classpaths = paths;
  }

  public void setTypeLoaders( List<String> loaders )
  {
    _typeLoaders = loaders;
  }
}
