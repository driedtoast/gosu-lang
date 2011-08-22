package gw.lang.reflect.gs;

import gw.util.fingerprint.FP64;
import gw.lang.tidb.ITypeInfoDatabaseIndexableType;
import gw.lang.parser.IScriptPartId;

import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuResource extends ITypeInfoDatabaseIndexableType
{
  /**
   * @return A list of source roots this resource manages. For instance, a
   *         Gosu class has only one source root -- the source for the entire
   *         class. A ruleset has two source roots; one for the condition and
   *         one for the action. Essentially each distinct chunk of Gosu in a
   *         resource will have a separate source root so that it can be uniquely
   *         identified in cooperation with search tools and intellisense tools.
   */
  public List<? extends ISourceRoot> getSourceRoots();

  /**
   * @return true if this resource should be indexed in the type info database
   */
  public boolean shouldIndex();

  public FP64 getFingerprint();

  public void update( IScriptPartId partId, String strSource );

  /**
   * @return the fqn of this resource
   */
  public String getName();
}
