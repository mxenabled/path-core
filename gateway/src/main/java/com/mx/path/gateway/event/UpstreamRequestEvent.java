package com.mx.path.gateway.event;

import com.mx.path.core.common.connect.Request;

/**
 * Request to upstream service event.
 */
public interface UpstreamRequestEvent {
  /**
   * Return request.
   *
   * @return request
   */
  Request getRequest();
}
