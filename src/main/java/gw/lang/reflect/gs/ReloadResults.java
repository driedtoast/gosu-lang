package gw.lang.reflect.gs;

import java.util.List;

/**
 * The ReloadResults class contains the results of an attempt to redefine classes, including whether or not the
 * reload succeed, any associated error message, and the names of the classes that were reloaded (in the case of
 * success) or that an attempt was made to reload (if the reload wasn't successful).
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class ReloadResults {
  
  public enum ReloadStatus {
    SUCCESS,
    NOT_ATTEMPTED,
    PARSE_FAILURE,
    REDEFINE_REJECTED,
    REDEFINE_ERROR,
    OTHER_ERROR
  }

  private ReloadStatus _status;
  private List<String> _classNames;
  private String _errorMessage;

  public ReloadResults(ReloadStatus status, List<String> classNames, String errorMessage) {
    _status = status;
    _classNames = classNames;
    _errorMessage = errorMessage;
  }

  public ReloadStatus getStatus() {
    return _status;
  }

  public List<String> getClassNames() {
    return _classNames;
  }

  public String getErrorMessage() {
    return _errorMessage;
  }
}
