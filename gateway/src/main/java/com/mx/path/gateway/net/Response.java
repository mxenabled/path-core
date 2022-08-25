package com.mx.path.gateway.net;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @deprecated Use {@link com.mx.common.connect.Response}
 */
@Deprecated
@SuppressFBWarnings("NM_SAME_SIMPLE_NAME_AS_SUPERCLASS")
public class Response extends com.mx.common.connect.Response<Request, Response> {
  public Response() {
    super();
  }

  public Response(Request request) {
    super();
    withRequest(request);
  }
}
