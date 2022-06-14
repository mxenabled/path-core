package com.mx.accessors;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.mx.common.lang.Strings;

/**
 * Represents exception raised from an accessor. Includes contextual information which can be
 * attached to logs and exception reports.
 */
public class AccessorException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  @Getter
  @Setter
  private AccessorResponseStatus status;
  @Getter
  @Setter
  private String clientId;
  @Getter
  @Setter
  private String code;
  @Getter
  @Setter
  private String endpointKey;
  @Getter
  @Setter
  private String errorKey;
  @Getter
  @Setter
  private String userMessage;
  @Setter
  private boolean report;
  @Getter
  @Setter
  private String reason;
  @Setter
  private String message;
  @Getter
  @Setter
  private String titleMessage;

  @Getter
  private final Map<String, String> headers = new LinkedHashMap<>();

  /**
   * Eception used for stubbing methods that are not implemented in base interfaces
   *
   * @param status - AccessorResponseStatus (usually NOT_IMPLEMENTED)
   */
  public AccessorException(AccessorResponseStatus status) {
    this(null, null, status, null, null, false, null);
  }

  /**
   * Exception used to wrap INTERNAL exceptions encountered by Middleware
   *
   * @param message     - String indicating failure (internal facing only)
   * @param status      - AccessorResponseStatus of exception
   * @param reportError - boolean that determines if this exception will be reported to exception tracking service
   * @param cause       - Throwable that AccessorException wraps
   */
  public AccessorException(String message, AccessorResponseStatus status, boolean reportError, Throwable cause) {
    this(message, null, status, null, null, reportError, cause);
  }

  /**
   * Exception used to wrap "recoverable" exceptions encountered by Middleware (leverage client configurable userMessage via endpointKey and errorKey)
   *
   * @param message     - String indicating failure (internal facing only)
   * @param clientId    - String indicating which client failure is related to
   * @param status      - AccessorResponseStatus of exception
   * @param endpointKey - String key (1 of 2) that maps custom user error messaging by client in client_errors.yml
   * @param errorKey    - String key (2 of 2) that maps to custom user error messaging by client in client_errors.yml
   * @param reportError - boolean that determines if this exception will be reported to exception tracking service
   * @param cause       - Throwable that AccessorException wraps
   */
  public AccessorException(String message, String clientId, AccessorResponseStatus status, String endpointKey, String errorKey, boolean reportError, Throwable cause) {
    super(cause);
    this.setClientId(clientId);
    this.setEndpointKey(endpointKey);
    this.setErrorKey(errorKey);
    this.setStatus(status);
    this.setReport(reportError);
    if (Strings.isNotBlank(message)) {
      this.setMessage(message);
    }
  }

  /**
   * Exception used to wrap "recoverable" exceptions encountered by Middleware (leverage client configurable userMessage via endpointKey and errorKey)
   *
   * @param message      - String indicating failure (internal facing only)
   * @param clientId     - String indicating which client failure is related to
   * @param status       - AccessorResponseStatus of exception
   * @param titleMessage - Title Message that needs to be displayed on the mobile
   * @param endpointKey  - String key (1 of 2) that maps custom user error messaging by client in client_errors.yml
   * @param errorKey     - String key (2 of 2) that maps to custom user error messaging by client in client_errors.yml
   * @param reportError  - boolean that determines if this exception will be reported to exception tracking service
   * @param cause        - Throwable that AccessorException wraps
   */
  @SuppressWarnings("checkstyle:ParameterNumber")
  public AccessorException(String message, String clientId, AccessorResponseStatus status, String titleMessage, String endpointKey, String errorKey, boolean reportError, Throwable cause) {
    super(cause);
    this.setClientId(clientId);
    this.setEndpointKey(endpointKey);
    this.setErrorKey(errorKey);
    this.setStatus(status);
    this.setReport(reportError);
    if (Strings.isNotBlank(titleMessage)) {
      this.setTitleMessage(titleMessage);
    }
    if (Strings.isNotBlank(message)) {
      this.setMessage(message);
    }
  }

  /**
   * Used to append reason to userMessage
   *
   * @param newReason - String newReason for exception (for userMessage)
   * @return - AccessorException with newReason
   */
  public final AccessorException withReason(String newReason) {
    this.setReason(newReason);

    return this;
  }

  /**
   * Used to append MDX API error code to exception
   *
   * @param newCode - String api error code
   * @return - AccessorException with newCode
   */
  public final AccessorException withCode(String newCode) {
    setCode(newCode);

    return this;
  }

  /**
   * Used to add a header to be sent with error
   * @param name
   * @param value
   * @return
   */
  public final AccessorException withHeader(String name, String value) {
    this.headers.put(name, value);

    return this;
  }

  /**
   * Report accessor fct for boolean
   *
   * @return boolean report
   */
  public final boolean shouldReport() {
    return report;
  }

  /**
   * Get internal AccessorException message
   *
   * @return String message
   */
  @Override
  public final String getMessage() {
    return message;
  }
}
