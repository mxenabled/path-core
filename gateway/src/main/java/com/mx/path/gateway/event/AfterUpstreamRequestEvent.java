package com.mx.path.gateway.event;

import lombok.Builder;
import lombok.Getter;

import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.Response;
import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.Session;

/**
 * Fires after a request is executed.
 */
@Builder
@Getter
public class AfterUpstreamRequestEvent implements UpstreamRequestEvent {
  private Response<?, ?> response;
  private final RequestContext requestContext;
  private final Session session;

  /**
   * Return event request.
   *
   * @return request
   */
  @Override
  public final Request getRequest() {
    if (getResponse() == null) {
      return null;
    }
    return getResponse().getRequest();
  }
}
