package com.mx.path.gateway.event;

import com.mx.path.core.common.connect.Request;

public interface UpstreamRequestEvent {
  Request getRequest();
}
