package com.mx.path.gateway.event;

import lombok.Builder;
import lombok.Data;

import com.mx.path.core.common.connect.Request;

/**
 * Fires before a request is executed.
 */
@Builder
@Data
public class BeforeUpstreamRequestEvent implements UpstreamRequestEvent {
  private Request request;
}
