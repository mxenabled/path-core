package com.mx.path.gateway.connect.filters;

import com.mx.common.connect.Request;
import com.mx.common.connect.RequestFilterBase;
import com.mx.common.connect.Response;
import com.mx.common.exception.ConnectException;
import com.mx.common.http.HttpStatus;
import com.mx.common.process.FaultTolerantExecutor;
import com.mx.path.gateway.context.GatewayRequestContext;
import com.mx.path.model.context.RequestContext;
import com.mx.path.model.context.facility.Facilities;

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
      } catch (ConnectException e) {
        response.withBody("** MX INTERNAL FAULT TOLERANCE ERROR: " + e.getStatus().name() + " **");
        response.withStatus(e.getStatus().toHttpStatus());
        throw e;
      } catch (Exception e) {
        response.withBody("** MX INTERNAL FAULT TOLERANCE ERROR **");
        response.withStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        throw new ConnectException("Upstream API request failure", e);
      }
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
}
