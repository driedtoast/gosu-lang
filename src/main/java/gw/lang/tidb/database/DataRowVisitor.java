package gw.lang.tidb.database;

import java.nio.ByteBuffer;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface DataRowVisitor {

  /**
   * Visits each row in the table (full table scan).
   * 
   * @param currentPosition
   */
  public void visit(ByteBuffer b, int rowNum, int currentPosition);
}
