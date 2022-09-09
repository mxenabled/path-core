package com.mx.path.gateway.events;

import lombok.Builder;
import lombok.Data;

import com.mx.common.connect.Request;

/**
 * Fires before a request is executed
 */
@Builder
@Data
public class BeforeUpstreamRequestEvent implements UpstreamRequestEvent {
  private Request request;
}
