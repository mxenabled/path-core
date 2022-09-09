package com.mx.path.gateway.util;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.mx.common.http.HttpStatus;
import com.mx.common.lang.Strings;

/**
 * @deprecated This is moved here for backward compatibility. It will be removed. Use {@link com.mx.common.connect.ConnectException}
 */
@Deprecated
public class MdxApiException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  @Getter
  @Setter
  private HttpStatus status;
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
  private String errorTitle;

  @Getter
  private final Map<String, String> headers = new LinkedHashMap<>();

  /**
   * Exception used for stubbing methods that are not implemented in base interfaces
   *
   * @param status - HttpStatus (usually NOT_IMPLEMENTED)
   */
  public MdxApiException(HttpStatus status) {
    this(null, null, status, null, null, false, null);
  }

  /**
   * Exception used to wrap exceptions encountered by Middleware API (ignore client configurable userMessage)
   *
   * @param message     - String indicating failure (internal facing only)
   * @param userMessage - String indicating failure (client facing)
   * @param status      - HttpStatus of exception
   * @param reportError - boolean that determines if this exception will be reported to exception tracking service
   * @param cause       - Throwable that MdxApiException wraps
   */
  @Deprecated
  public MdxApiException(String message, String userMessage, HttpStatus status, boolean reportError, Throwable cause) {
    this(message, null, status, null, null, reportError, cause);
    if (Strings.isNotBlank(userMessage)) {
      this.setUserMessage(userMessage);
    }
  }

  /**
   * Exception used to wrap INTERNAL exceptions encountered by Middleware
   *
   * @param message     - String indicating failure (internal facing only)
   * @param status      - HttpStatus of exception
   * @param reportError - boolean that determines if this exception will be reported to exception tracking service
   * @param cause       - Throwable that MdxApiException wraps
   */
  public MdxApiException(String message, HttpStatus status, boolean reportError, Throwable cause) {
    this(message, null, status, null, null, reportError, cause);
  }

  /**
   * Exception used to wrap "recoverable" exceptions encountered by Middleware (leverage client configurable userMessage via endpointKey and errorKey)
   *
   * @param message     - String indicating failure (internal facing only)
   * @param clientId    - String indicating which client failure is related to
   * @param status      - HttpStatus of exception
   * @param endpointKey - String key (1 of 2) that maps custom user error messaging by client in client_errors.yml
   * @param errorKey    - String key (2 of 2) that maps to custom user error messaging by client in client_errors.yml
   * @param reportError - boolean that determines if this exception will be reported to exception tracking service
   * @param cause       - Throwable that MdxApiException wraps
   */
  public MdxApiException(String message, String clientId, HttpStatus status, String endpointKey, String errorKey, boolean reportError, Throwable cause) {
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
   * @param status       - HttpStatus of exception
   * @param errorTitle   - Title Message that needs to be displayed on the mobile
   * @param endpointKey  - String key (1 of 2) that maps custom user error messaging by client in client_errors.yml
   * @param errorKey     - String key (2 of 2) that maps to custom user error messaging by client in client_errors.yml
   * @param reportError  - boolean that determines if this exception will be reported to exception tracking service
   * @param cause        - Throwable that MdxApiException wraps
   */
  @SuppressWarnings("checkstyle:ParameterNumber")
  public MdxApiException(String message, String clientId, HttpStatus status, String errorTitle, String endpointKey, String errorKey, boolean reportError, Throwable cause) {
    super(cause);
    this.setClientId(clientId);
    this.setEndpointKey(endpointKey);
    this.setErrorKey(errorKey);
    this.setStatus(status);
    this.setReport(reportError);
    if (Strings.isNotBlank(errorTitle)) {
      this.setErrorTitle(errorTitle);
    }
    if (Strings.isNotBlank(message)) {
      this.setMessage(message);
    }
  }

  /**
   * Used to add a header to be sent with error
   * @param name
   * @param value
   * @return
   */
  public final MdxApiException withHeader(String name, String value) {
    this.headers.put(name, value);

    return this;
  }

  /**
   * Used to append reason to userMessage
   *
   * @param newReason - String newReason for exception (for userMessage)
   * @return - MdxApiException with newReason
   */
  public final MdxApiException withReason(String newReason) {
    this.setReason(newReason);

    return this;
  }

  /**
   * Used to append MDX API error code to exception
   *
   * @param newCode - String api error code
   * @return - MdxApiException with newCode
   */
  public final MdxApiException withCode(String newCode) {
    setCode(newCode);

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
   * Get internal MdxApiException message
   *
   * @return String message
   */
  @Override
  public final String getMessage() {
    return message;
  }
}