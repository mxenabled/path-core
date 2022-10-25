package com.mx.path.gateway.connect.filters;

import com.mx.common.connect.Request;
import com.mx.common.connect.RequestFilterBase;
import com.mx.common.connect.Response;

/**
 * Runs callbacks
 *
 * <p>Runs runs next, records duration, runs complete callback, runs process callback
 */
public class CallbacksFilter extends RequestFilterBase {
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
