package com.mx.path.gateway.connect.filters;

import com.mx.common.connect.Request;
import com.mx.common.connect.RequestFilterBase;
import com.mx.common.connect.Response;
import com.mx.common.exception.request.accessor.connect.ConnectException;
import com.mx.path.gateway.util.UpstreamLogger;

/**
 * Handles request exceptions
 * <p>
 * Runs next
 * If Connection Failure, log call and rethrow
 * All other exceptions, placed in response
 * </p>
 */
public class ErrorHandlerFilter extends RequestFilterBase {

  // Statics

  private static UpstreamLogger upstreamLogger = new UpstreamLogger();

  public static void resetUpstreamLogger() {
    ErrorHandlerFilter.upstreamLogger = new UpstreamLogger();
  }

  public static void setUpstreamLogger(UpstreamLogger upstreamLogger) {
    ErrorHandlerFilter.upstreamLogger = upstreamLogger;
  }

  // Public

  @Override
  public final void execute(Request request, Response response) {
    try {
      next(request, response);
    } catch (ConnectException e) {
      response.withException(e);

      upstreamLogger.logRequest(response);

      // Bubble up connection failures ONLY
      throw e;
    } catch (Exception e) {
      response.withException(e);
    }
  }
}
