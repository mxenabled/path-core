package com.mx.path.gateway.configuration;

import lombok.Builder;
import lombok.Data;

import com.mx.path.gateway.Gateway;

/**
 * Event
 */
@Builder
@Data
public class GatewayCreatedEvent {
  private Gateway<?> gateway;
}
