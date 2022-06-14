package com.mx.path.api.connect.messaging.remote;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.common.lang.Strings;
import com.mx.common.security.LogValueMasker;
import com.mx.path.api.connect.messaging.MessageRequest;
import com.mx.path.api.connect.messaging.MessageResponse;
import com.mx.path.model.context.RequestContext;
import com.mx.path.model.context.tracing.CustomTracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

// todo: Come up with a better name for this.
public class RemoteLogger {
  private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
  private static final LogValueMasker LOGMASKER = new LogValueMasker();
  private static Logger logger = LoggerFactory.getLogger(RemoteLogger.class);

  public static void setLogger(Logger logger) {
    RemoteLogger.logger = logger;
  }

  public static void resetLogger() {
    logger = LoggerFactory.getLogger(RemoteLogger.class);
  }

  @SuppressWarnings("PMD.CyclomaticComplexity")
  public final void logRequest(MessageResponse response) {
    RequestContext requestContext = RequestContext.current();
    MDC.put("client_guid", requestContext.getClientGuid());
    MDC.put("client_id", requestContext.getClientId());
    if (requestContext.getUserGuid() != null) {
      MDC.put("user_guid", requestContext.getUserGuid());
    } else {
      MDC.remove("user_guid");
    }
    if (requestContext.getUserId() != null) {
      MDC.put("user_id", requestContext.getUserId());
    } else {
      MDC.remove("user_id");
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

    MessageRequest request = response.getRequest();
    MDC.put("request_operation", request.getOperation());
    MDC.put("request_channel", request.getChannel());
    MDC.put("request_uri", request.getChannel());

    if (request.getMessageHeaders() != null) {
      MDC.put("span_id", CustomTracer.getSpanId());
      MDC.put("trace_id", CustomTracer.getTraceId());

      Map<String, String> maskedRequestHeaders = maskHeaders(request.getMessageHeaders().getHeaders());
      MDC.put("request_headers_json", GSON.toJson(maskedRequestHeaders));
      MDC.put("request_headers", buildHeaderString(maskedRequestHeaders));
    } else {
      MDC.remove("request_headers_json");
      MDC.remove("request_headers");
    }

    if (request.getMessageParameters() != null) {
      Map<String, String> queryParams = request.getMessageParameters().getParameters();
      MDC.put("query_params", buildHeaderString(maskHeaders(queryParams)));
    } else {
      MDC.remove("query_params");
    }

    if (request.getBody() != null) {
      MDC.put("request_body", LOGMASKER.maskPayload(request.getBody().toString()));
    } else {
      MDC.remove("request_body");
    }

    MDC.put("request_duration", String.valueOf(request.getDuration()));

    if (response.getStatus() != null) {
      MDC.put("status", String.valueOf(response.getStatus()));
    } else {
      MDC.remove("status");
    }

    if (Strings.isNotBlank(response.getBody())) {
      MDC.put("response_body", LOGMASKER.maskPayload(response.getBody()));
    } else {
      MDC.remove("response_body");
    }

    if (response.getMessageHeaders() != null) {
      Map<String, String> maskedResponseHeaders = maskHeaders(response.getMessageHeaders().getHeaders());
      MDC.put("response_headers_json", GSON.toJson(maskedResponseHeaders));
      MDC.put("response_headers", buildHeaderString(maskedResponseHeaders));
    } else {
      MDC.remove("response_headers_json");
      MDC.remove("response_headers");
    }

    MDC.put("api_call_payload", buildApiCallPayload(request, response));

    if (Strings.isNotBlank(response.getError())) {
      MDC.put("response_error_message", response.getError());
    } else {
      MDC.remove("response_error_message");
    }

    if (response.getException() != null) {
      MDC.put("response_error_stack", Arrays.stream(response.getException().getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
      MDC.put("response_error_type", response.getException().getClass().getCanonicalName());
    } else {
      MDC.remove("response_error_stack");
      MDC.remove("response_error_type");
    }

    if (response.getException() != null) {
      logger.error("Remote request failed", response.getException());
    } else {
      logger.info("Remote Request");
    }

    // Can't clear MDC, in case something else is using it. Just clean up what we put in the MDC.
    MDC.remove("api_call_payload");
    MDC.remove("client_guid");
    MDC.remove("client_id");
    MDC.remove("request_body");
    MDC.remove("request_channel");
    MDC.remove("request_uri");
    MDC.remove("request_duration");
    MDC.remove("request_headers");
    MDC.remove("request_headers_json");
    MDC.remove("response_body");
    MDC.remove("response_headers");
    MDC.remove("response_headers_json");
    MDC.remove("response_error_message");
    MDC.remove("response_error_stack");
    MDC.remove("response_error_type");
    MDC.remove("session_trace_id");
    MDC.remove("span_id");
    MDC.remove("status");
    MDC.remove("trace_id");
  }

  private String buildApiCallPayload(MessageRequest request, MessageResponse response) {
    StringBuilder body = new StringBuilder();
    body.append("= Remote Request\n");
    body.append("channel: ").append(request.getChannel()).append("\n");
    if (request.getMessageHeaders() != null) {
      body.append(buildHeaderString(request.getMessageHeaders().getHeaders()));
      body.append("\n");
    }
    if (Strings.isNotBlank(request.getBody())) {
      body.append("\n");
      body.append(LOGMASKER.maskPayload(request.getBody()));
    }
    body.append("\n= Remote Response\n");
    body.append("status: ").append(response.getStatus()).append("\n");
    if (response.getMessageHeaders() != null) {
      body.append(buildHeaderString(response.getMessageHeaders().getHeaders()));
      body.append("\n");
    }
    if (Strings.isNotBlank(response.getBody())) {
      body.append("\n");
      body.append(LOGMASKER.maskPayload(response.getBody()));
    }
    body.append("\n\n");

    return body.toString();
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
}
