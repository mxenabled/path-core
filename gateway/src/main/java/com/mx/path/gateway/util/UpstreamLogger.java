package com.mx.path.gateway.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.path.core.common.collection.MultiValueMap;
import com.mx.path.core.common.connect.Request;
import com.mx.path.core.common.connect.Response;
import com.mx.path.core.common.security.LogValueMasker;
import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.Session;
import com.mx.path.gateway.context.Scope;

import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class UpstreamLogger {

  // Constants

  private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
  private static final LogValueMasker LOGMASKER = new LogValueMasker();

  // Statics

  private static org.slf4j.Logger logger = LoggerFactory.getLogger(UpstreamLogger.class);

  public static void setLogger(org.slf4j.Logger logger) {
    UpstreamLogger.logger = logger;
  }

  public static void resetLogger() {
    UpstreamLogger.logger = LoggerFactory.getLogger(UpstreamLogger.class);
  }

  // Public

  @SuppressWarnings("PMD.CyclomaticComplexity")
  public final void logRequest(Response response) {

    RequestContext requestContext = RequestContext.current();
    MDC.put("client_guid", requestContext.getClientGuid());
    MDC.put("client_id", requestContext.getClientId());
    MDC.put("path", requestContext.getPath());
    if (requestContext.getUserGuid() != null) {
      MDC.put("user_guid", requestContext.getUserGuid());
    } else {
      MDC.remove("user_guid");
    }
    if (Session.current() != null && Session.current().getUserId() != null) {
      MDC.put("user_id", Session.current().getUserId());
    } else if (requestContext.getUserId() != null) {
      MDC.put("user_id", requestContext.getUserId());
    } else {
      MDC.remove("user_id");
    }
    if (Session.current() != null && Session.current().get(Scope.Session, "login") != null) {
      MDC.put("login", Session.current().get(Scope.Session, "login"));
    } else {
      MDC.remove("login");
    }
    if (requestContext.getFeature() != null) {
      MDC.put("feature", requestContext.getFeature());
    } else {
      MDC.remove("feature");
    }
    if (requestContext.getOriginatingIP() != null) {
      MDC.put("ip_address", requestContext.getOriginatingIP());
    } else {
      MDC.remove("ip_address");
    }
    if (requestContext.getSessionTraceId() != null) {
      MDC.put("session_trace_id", requestContext.getSessionTraceId());
    } else {
      MDC.remove("session_trace_id");
    }
    if (requestContext.getDeviceTraceId() != null) {
      MDC.put("device_trace_id", requestContext.getDeviceTraceId());
    } else {
      MDC.remove("device_trace_id");
    }

    Request request = response.getRequest();
    MDC.put("request_method", request.getMethod());
    MDC.put("request_uri", request.getUri());
    MDC.put("span_id", request.getTraceSpanId());
    MDC.put("trace_id", request.getTraceId());

    if (request.getHeaders() != null) {
      Map<String, String> maskedRequestHeaders = maskHeaders(request.getHeaders());
      MDC.put("request_headers_json", GSON.toJson(maskedRequestHeaders));
      MDC.put("request_headers", buildHeaderString(maskedRequestHeaders));
    } else {
      MDC.remove("request_headers_json");
      MDC.remove("request_headers");
    }

    if (request.getQueryStringParams() != null) {
      Map<String, String> queryParams = request.getQueryStringParams();
      MDC.put("query_params", buildHeaderString(maskHeaders(queryParams)));
    } else {
      MDC.remove("query_params");
    }

    if (request.getBodyJson() != null) {
      MDC.put("request_body", LOGMASKER.maskPayload(request.getBodyJson()));
    } else if (request.getFormBody() != null) {
      MDC.put("request_body", LOGMASKER.maskPayload(encodeFormData(request.getFormBody().toForm())));
    } else if (request.getBody() != null) {
      MDC.put("request_body", LOGMASKER.maskPayload(request.getBody().toString()));
    } else {
      MDC.remove("request_body");
    }

    MDC.put("api_call_payload", buildApiPayload(response, request));
    MDC.put("request_attempt", String.valueOf(response.getAttempt()));

    if (response.getDuration() != null) {
      MDC.put("request_duration", String.valueOf(response.getDuration().toMillis()));
    } else {
      MDC.remove("request_duration");
    }

    if (response.getStatus() != null) {
      MDC.put("status", String.valueOf(response.getStatus().value()));
    } else {
      MDC.remove("status");
    }

    if (response.hasBody()) {
      MDC.put("response_body", LOGMASKER.maskPayload(response.getBody()));
    } else {
      MDC.remove("response_body");
    }

    if (response.getHeaders() != null) {
      Map<String, String> maskedResponseHeaders = maskHeaders(response.getHeaders());
      MDC.put("response_headers_json", GSON.toJson(maskedResponseHeaders));
      MDC.put("response_headers", buildHeaderString(maskedResponseHeaders));
    } else {
      MDC.remove("response_headers_json");
      MDC.remove("response_headers");
    }

    MDC.put("log_guid", UUID.randomUUID().toString());

    if (response.getException() != null) {
      Exception exception = response.getException();
      MDC.put("exception", LoggingExceptionFormatter.formatLoggingExceptionWithStacktrace(exception));
      logger.error("Upstream request failed", exception);
    } else {
      MDC.remove("exception");
      logger.info("Upstream Request");
    }

    // Can't clear MDC, in case something else is using it. Just clean up what we put in the MDC.
    MDC.remove("api_call_payload");
    MDC.remove("client_guid");
    MDC.remove("client_id");
    MDC.remove("device_trace_id");
    MDC.remove("exception");
    MDC.remove("parent_id");
    MDC.remove("path");
    MDC.remove("request_attempt");
    MDC.remove("request_body");
    MDC.remove("request_duration");
    MDC.remove("request_headers");
    MDC.remove("request_headers_json");
    MDC.remove("request_method");
    MDC.remove("request_uri");
    MDC.remove("response_body");
    MDC.remove("response_headers");
    MDC.remove("response_headers_json");
    MDC.remove("session_trace_id");
    MDC.remove("status");
    MDC.remove("span_id");
    MDC.remove("trace_id");
    MDC.remove("log_guid");
  }

  private String buildHeaderString(Map<String, String> headers) {
    StringBuilder headerStr = new StringBuilder();
    headers.forEach((name, value) -> {
      headerStr.append(name);
      headerStr.append(": ");
      headerStr.append(value);
      headerStr.append("\n");
    });

    return headerStr.toString();
  }

  private Map<String, String> maskHeaders(Map<String, String> headers) {
    if (headers == null) {
      return null;
    }
    Map<String, String> maskedHeaders = new HashMap<>();
    headers.forEach((name, value) -> maskedHeaders.put(name, LOGMASKER.maskHeaderValue(name, value)));
    return maskedHeaders;
  }

  private String buildApiPayload(Response response, Request request) {
    StringBuilder b = new StringBuilder();
    b.append("= Request\n\n");
    b.append(request.getMethod());
    b.append(" ");
    b.append(request.getUri());
    b.append("\n\n");
    Map<String, String> requestHeaders = maskHeaders(request.getHeaders());
    if (requestHeaders != null) {
      b.append(buildHeaderString(requestHeaders));
    }
    Map<String, String> requestQueries = request.getQueryStringParams();
    if (requestQueries != null) {
      b.append(buildHeaderString(maskHeaders(requestQueries)));
    }

    if (request.getBodyJson() != null) {
      b.append("\n");
      b.append(LOGMASKER.maskPayload(request.getBodyJson()));
      b.append("\n");
    } else if (request.getFormBody() != null) {
      b.append("\n");
      b.append(LOGMASKER.maskPayload(encodeFormData(request.getFormBody().toForm())));
      b.append("\n");
    } else if (request.getBody() != null) {
      b.append("\n");
      b.append(LOGMASKER.maskPayload(request.getBody().toString()));
      b.append("\n");
    }

    b.append("\n= Response\n\n");
    b.append(response.getStatus());
    b.append("\n");
    Map<String, String> responseHeaders = maskHeaders(response.getHeaders());
    if (responseHeaders != null) {
      b.append(buildHeaderString(responseHeaders));
    }

    if (response.hasBody()) {
      b.append(LOGMASKER.maskPayload(response.getBody()));
    }

    return b.toString();
  }

  private String encodeFormData(MultiValueMap<String, String> data) {
    return data.toSingleValueMap().entrySet().stream().map(e -> {
      try {
        return e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8.toString());
      } catch (Exception ex) {
        logger.error("Unable to encode form data for logging", ex);
      }
      return null;
    }).filter(Objects::nonNull).collect(Collectors.joining("&"));
  }
}
