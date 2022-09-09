package com.mx.path.gateway.events;

import lombok.Builder;
import lombok.Getter;

import com.mx.common.connect.Request;
import com.mx.common.connect.Response;
import com.mx.path.model.context.RequestContext;
import com.mx.path.model.context.Session;

/**
 * Fires after a request is executed
 */
@Builder
@Getter
public class AfterUpstreamRequestEvent implements UpstreamRequestEvent {
  private Response<?, ?> response;
  private final RequestContext requestContext;
  private final Session session;

  @Override
  public final Request getRequest() {
    if (getResponse() == null) {
      return null;
    }
    return getResponse().getRequest();
  }
}
