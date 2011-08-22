package gw.lang.ir;

import gw.lang.UnstableAPI;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRAnnotation {

  private IRType _descriptor;
  private boolean _include;
  private Object _value;

  public IRAnnotation(IRType descriptor, boolean include, Object value) {
    _descriptor = descriptor;
    _include = include;
    _value = value;
  }

  public IRType getDescriptor() {
    return _descriptor;
  }

  public boolean isInclude() {
    return _include;
  }

  public Object getValue()
  {
    return _value;
  }
}
