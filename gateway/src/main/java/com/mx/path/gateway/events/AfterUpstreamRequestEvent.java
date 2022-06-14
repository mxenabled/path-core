package com.mx.path.gateway.events;

import lombok.Builder;
import lombok.Data;

import com.mx.path.gateway.net.Response;

@Builder
@Data
public class AfterUpstreamRequestEvent {
  private Response response;
}
