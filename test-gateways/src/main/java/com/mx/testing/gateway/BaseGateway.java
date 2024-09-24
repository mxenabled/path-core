package com.mx.testing.gateway;

import lombok.experimental.SuperBuilder;

import com.mx.path.core.common.gateway.GatewayBaseClass;
import com.mx.path.gateway.Gateway;
import com.mx.path.gateway.configuration.RootGateway;
import com.mx.testing.accessors.BaseAccessor;

/**
 * Represents the base class for gateways, providing common functionality for gateway operations.
 */
@RootGateway
@SuperBuilder
@GatewayBaseClass(target = BaseAccessor.class, namespace = "com.mx.testing.gateway.api", className = "Gateway")
public class BaseGateway extends Gateway<BaseAccessor> {

  /**
   * Default constructor for {@link BaseGateway}.
   */
  public BaseGateway() {
  }

  /**
   * Constructs a {@link BaseGateway} with the specified client ID.
   *
   * @param clientId client ID to be used on this gateway instance
   */
  public BaseGateway(String clientId) {
    super(clientId);
  }
}
