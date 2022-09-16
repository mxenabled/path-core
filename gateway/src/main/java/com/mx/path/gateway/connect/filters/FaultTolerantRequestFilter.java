package com.mx.path.gateway.connect.filters;

import java.net.SocketTimeoutException;

import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.connect.Request;
import com.mx.common.connect.RequestFilterBase;
import com.mx.common.connect.Response;
import com.mx.common.http.HttpStatus;
import com.mx.common.process.FaultTolerantExecutionException;
import com.mx.common.process.FaultTolerantExecutor;
import com.mx.path.gateway.context.GatewayRequestContext;
import com.mx.path.gateway.net.HystrixConfigurations;
import com.mx.path.gateway.net.HystrixRequestWrapper;
import com.mx.path.gateway.net.UpstreamConnectionException;
import com.mx.path.gateway.util.MdxApiException;
import com.mx.path.model.context.RequestContext;
import com.mx.path.model.context.facility.Facilities;
import com.netflix.hystrix.exception.HystrixRuntimeException;

/**
 * Wraps the upstream call in a FaultTolerantExecutor if one is configured. Otherwise, uses the configured hystrix wrapper
 * (if enabled) and calls next.
 */
public class FaultTolerantRequestFilter extends RequestFilterBase {
  @SuppressWarnings("PMD.CyclomaticComplexity")
  @Override
  public final void execute(Request request, Response response) {
    FaultTolerantExecutor faultTolerantExecutor = Facilities.getFaultTolerantExecutor(RequestContext.current().getClientId());
    if (faultTolerantExecutor != null) {
      try {
        String scope = request.getFaultTolerantScope() != null ? request.getFaultTolerantScope() : buildScope();
        faultTolerantExecutor.submit(scope, scopeConfigurations -> {
          // If a request timeout was explicitly provided (should be rare) we respect it. Otherwise, we use the timeout
          // that the FaultTolerantExecutor is using for this request.
          if (request.getRequestTimeOut() == null) {
            request.withTimeOut(scopeConfigurations.getTimeout());
          }
          next(request, response);
          return null;
        });
      } catch (FaultTolerantExecutionException e) {
        response.withBody("** MX INTERNAL FAULT TOLERANCE ERROR: " + e.getStatus().name() + " **");

        // If the underlying exception occurred in the `HttpClientExecutor` we want to try and translate that exception
        // into something more helpful than a 500 if possible.
        if (e.getCause() instanceof UpstreamConnectionException && e.getCause().getCause() instanceof SocketTimeoutException) {
          response.withStatus(HttpStatus.GATEWAY_TIMEOUT);
          throw new MdxApiException("Upstream socket timeout", HttpStatus.GATEWAY_TIMEOUT, false, e);
        } else if (e.getCause() instanceof MdxApiException) {
          MdxApiException cause = (MdxApiException) e.getCause();
          response.withStatus(cause.getStatus());
          throw cause;
        }

        response.withStatus(e.getStatus().toHttpStatus());
        if (e.getStatus() == PathResponseStatus.INTERNAL_ERROR) {
          throw new MdxApiException("Upstream API request failure", e.getStatus().toHttpStatus(), false, e.getCause());
        } else {
          throw new MdxApiException("Upstream API request failure", e.getStatus().toHttpStatus(), false, e);
        }
      } catch (Exception e) {
        response.withBody("** MX INTERNAL FAULT TOLERANCE ERROR **");
        response.withStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        throw new MdxApiException("Upstream API request failure", HttpStatus.INTERNAL_SERVER_ERROR, false, e);
      }
    } else if (HystrixConfigurations.isEnableHystrixWrapper()) {
      handleHystrixCircuitBreaker(request, response);
    } else {
      next(request, response);
    }
  }

  final String buildScope() {
    StringBuilder sb = new StringBuilder();
    sb.append("http");

    if (RequestContext.current().getFeature() != null) {
      sb.append(".");
      sb.append(RequestContext.current().getFeature());
    }

    if (GatewayRequestContext.current() != null && GatewayRequestContext.current().getOp() != null) {
      sb.append(".");
      sb.append(GatewayRequestContext.current().getOp());
    }

    return sb.toString();
  }

  @SuppressWarnings("PMD.CyclomaticComplexity")
  final void handleHystrixCircuitBreaker(Request request, Response response) {
    HystrixRequestWrapper<Response> wrapper;
    if (request.getFeature() != null) {
      // NOTE: This is executed in another thread. ThreadLocal will not be the same from here down.
      wrapper = new HystrixRequestWrapper<>(() -> {
        next(request, response);

        return response;
      }, request.getRequestTimeOut() != null ? (int) request.getRequestTimeOut().toMillis() : HystrixConfigurations.getTimeoutInMs(), request.getFeature());
    } else {
      wrapper = new HystrixRequestWrapper<>(() -> {
        next(request, response);

        return response;
      }, request.getRequestTimeOut() != null ? (int) request.getRequestTimeOut().toMillis() : HystrixConfigurations.getTimeoutInMs(), request.getFeatureName());
    }

    try {
      wrapper.execute();
    } catch (HystrixRuntimeException e) {
      response.withBody("** MX INTERNAL HYSTRIX ERROR: " + e.getFailureType() + " **");
      HttpStatus status;
      String message;
      switch (e.getFailureType()) {
        case BAD_REQUEST_EXCEPTION:
          status = HttpStatus.INTERNAL_SERVER_ERROR;
          message = "Service sent a bad request";
          break;
        case TIMEOUT:
          status = HttpStatus.GATEWAY_TIMEOUT;
          message = "Upstream API timeout";
          break;
        case REJECTED_THREAD_EXECUTION:
          status = HttpStatus.TOO_MANY_REQUESTS;
          message = "Thread pool busy";
          break;
        case SHORTCIRCUIT:
          status = HttpStatus.SERVICE_UNAVAILABLE;
          message = "Circuit is open";
          request.setCircuitBreakerOpen(true);
          break;
        default:
          status = HttpStatus.INTERNAL_SERVER_ERROR;
          message = "Upstream API request failure";
          break;
      }

      // Fix up the status of the cause (which is an MDXApiException).
      // This will make the ErrorHandlerFilter work correctly.
      response.withStatus(status);
      Object fallback = e.getFallbackException();
      MdxApiException fallbackMdxApiException = fallback instanceof MdxApiException ? (MdxApiException) fallback : null;
      if (fallbackMdxApiException != null) {
        fallbackMdxApiException.setStatus(status);
        fallbackMdxApiException.setMessage(message);
        response.withStatus(status);
      }

      // todo: Change this to throw a different type of exception to uncouple us from Hystrix.
      throw e;
    }
  }
}
