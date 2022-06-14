package com.mx.accessors;

import com.mx.common.gateway.GatewayAPI;

/**
 * Accessor base for upstream system status
 *
 * <p>
 * Returns {@link AccessorResponseStatus#NO_CONTENT} if all is well.
 * </p>
 *
 * <p>
 * Returns {@link AccessorResponseStatus#SERVICE_UNAVAILABLE} if upstream system is unavailable
 * </p>
 *
 * <p>
 * Returns {@link AccessorResponseStatus#GATEWAY_TIMEOUT} if upstream system not responsive or circuit breaker is open
 * </p>
 */
@API(description = "Returns system status")
public abstract class StatusBaseAccessor extends Accessor {

  public StatusBaseAccessor(AccessorConfiguration configuration) {
    super(configuration);
  }

  /**
   * Get upstream system status
   * @return
   */
  @GatewayAPI
  @API(description = "Get upstream system status")
  public AccessorResponse<Void> get() {
    throw new AccessorException(AccessorResponseStatus.NOT_IMPLEMENTED);
  }
}
