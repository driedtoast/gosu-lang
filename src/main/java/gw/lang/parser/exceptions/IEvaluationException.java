

package gw.lang.parser.exceptions;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public abstract class IEvaluationException extends RuntimeException {

  protected IEvaluationException(String msg ) {
    super(msg);
  }

  protected IEvaluationException(Throwable t ) {
    super(t.getMessage(), t);
  }

  public abstract void setAdditionalDetails(String details);

}