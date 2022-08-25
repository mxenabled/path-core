package com.mx.path.gateway.events;

import lombok.Builder;
import lombok.Data;

import com.mx.common.connect.Request;

/**
 * Fires before connection make a request
 */
@Builder
@Data
public class BeforeUpstreamRequestEvent {
  private Request request;
}
