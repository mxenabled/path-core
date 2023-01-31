package com.mx.path.gateway.configuration;

import com.mx.common.gateway.GatewayBaseClass;
import com.mx.path.gateway.Gateway;

import java.util.Map;

/**
 * Implement to provide custom gateway initialization in Gateway flavour project
 *
 * <p>Specify the implementation using the {@link GatewayBaseClass} annotation
 *
 * <p>
 * todo: V2 - move to Gateway project and change param types from Object to Gateway
 */
public interface GatewayInitializer {
  default void afterGatewaysInitialized(Map<String, Gateway<?>> gateways) {
  }
}
