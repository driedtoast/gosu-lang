package gw.config;

/**
 * This interface facilitates registering custom implementations of
 * services in a ServiceKernel.  Typically a ServiceKernelInit class is constructed
 * reflectively based on a registry value.  See {@link ServiceKernel#redefineServicesWithClass(String)}.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface ServiceKernelInit
{
  /**
   * Allows the default kernel services to be overridden, providing
   * customizations at higher levels of the system. 
   */
  public void init( ServiceKernel kernel );

}