package com.mx.path.gateway.net.executors;

import com.mx.path.gateway.net.Request;
import com.mx.path.gateway.net.Response;

/**
 * Request decorator executor
 * <p>
 * Interface for all executors in decorator chain.
 * </p>
 * <p>
 * @deprecated functionality moved to connect package
 * </p>
 */
@Deprecated
public interface RequestExecutor {
  void execute(Request request, Response response);

  default Response execute(Request request) {
    Response response = new Response(request);
    execute(request, response);

    return response;
  }
}
