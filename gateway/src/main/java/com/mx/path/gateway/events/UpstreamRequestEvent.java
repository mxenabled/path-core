package com.mx.path.gateway.events;

import com.mx.common.connect.Request;

public interface UpstreamRequestEvent {
  Request getRequest();
}
