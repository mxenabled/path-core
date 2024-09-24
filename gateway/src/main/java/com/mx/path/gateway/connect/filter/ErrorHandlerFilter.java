package com.mx.path.gateway.connect.filter;

import com.mx.path.core.common.connect.ConnectException;
import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.RequestFilterBase;
import com.mx.path.core.common.connect.Response;
import com.mx.path.gateway.util.UpstreamLogger;

/**
 * Handles request exceptions.
 *
 * <pre>
 *   Runs next
 *     If Connection Failure, log call and rethrow
 *     All other exceptions, placed in response
 * </pre>
 */
public class ErrorHandlerFilter extends RequestFilterBase {

  // Statics

  private static UpstreamLogger upstreamLogger = new UpstreamLogger();

  /**
   * Create and set new instance of {@link UpstreamLogger}.
   */
  public static void resetUpstreamLogger() {
    ErrorHandlerFilter.upstreamLogger = new UpstreamLogger();
  }

  /**
   * Set upstream logger.
   *
   * @param upstreamLogger logger to set
   */
  public static void setUpstreamLogger(UpstreamLogger upstreamLogger) {
    ErrorHandlerFilter.upstreamLogger = upstreamLogger;
  }

  // Public

  /**
   * Execute request.
   *
   * @param request  Request
   * @param response Response
   */
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
