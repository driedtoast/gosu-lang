package gw.util;

/**
 * An callback interface for indicating progress during a lengthy operation.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IStagedProgressCallback extends IProgressCallback
{
  public void reset( int numStages, int[] percentages );
}