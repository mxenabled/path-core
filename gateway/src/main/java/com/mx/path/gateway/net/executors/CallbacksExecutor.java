package com.mx.path.gateway.net.executors;

import com.mx.path.gateway.net.Request;
import com.mx.path.gateway.net.Response;

/**
 * Runs callbacks
 *
 * <p>Runs runs next, records duration, runs complete callback, runs process callback
 *
 * @deprecated Use {@link com.mx.path.gateway.connect.filters.CallbacksFilter}
 */
@Deprecated
public class CallbacksExecutor extends RequestExecutorBase {
  public CallbacksExecutor(RequestExecutor next) {
    super(next);
  }

  @Override
  public final void execute(Request request, Response response) {
    next(request, response);

    // Fire complete callback
    request.completed(response);

    // Fire process lambda
    Object obj = request.process(response);
    response.withObject(obj);
  }
}
