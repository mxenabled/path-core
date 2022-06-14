package com.mx.path.gateway.events;

import lombok.Builder;
import lombok.Data;

import com.mx.path.gateway.net.Request;

@Builder
@Data
public class BeforeUpstreamRequestEvent {
  private Request request;
}
