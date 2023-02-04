package com.mx.path.gateway.configuration;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

import com.mx.path.gateway.Gateway;

@Builder
@Data
public class GatewaysInitializedEvent {
  private Map<String, Gateway<?>> gateways;
}
