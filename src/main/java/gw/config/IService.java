package gw.config;

/**
 * A service that is provided by the system.  All service implementations should provide a
 * very cheap constructor and do any heavy setup in the init() method.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IService
{
  /**
   * @return true if this service has been initialized, false otherwise
   */
  boolean isInited();

  /**
   * Initialize this service
   */
  void init();
}