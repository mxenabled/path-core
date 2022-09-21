package com.mx.common.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.mx.common.accessors.PathResponseStatus;

/**
 * Base exception for any request-based error. These are errors that occur at any level during a request.
 *
 * <p>
 * For Path system startup errors, use {@link PathSystemException}
 * </p>
 *
 * <p>
 * Hierarchy:
 * <pre>
 *   {@link GatewayException}
 *     {@link BehaviorException}
 *   {@link AccessorException}
 *     {@link AccessorUserException}
 *       {@link ResourceNotFoundException}
 *       {@link UnauthorizedException}
 *     {@link AccessorSystemException}
 *       {@link AccessorMethodNotImplementedException}
 *     {@link ConnectException}
 *       {@link TimeoutException}
 *       {@link ServiceUnavailableException}
 *         {@link CircuitOpenException}
 *       {@link TooManyRequestsException}
 *       {@link UpstreamErrorException}
 *   {@link FacilityException}
 * </pre>
 * </p>
 * <p>
 *   Each exception sets its own default Report and Status attributes. Most have constructors that allow setting
 *   fields common for their type. {@link PathRequestException} provides fluent setters for all attributes that will
 *   allow defaults to be overridden or additional attributes to be set.
 *
 *   Example:
 *   <pre>
 *     {@code
 *       throw new AccessorUserException("Identity unknown", "Username and/or password did not match system", PathResponseStatus.UNAUTHORIZED)
 *         .withReason("Bad password")
 *         .withCode("1401");
 *     }
 *   </pre>
 * </p>
 */
public abstract class PathRequestException extends RuntimeException {
  @Getter
  @Setter
  private String code;

  @Getter
  @Setter
  private String errorTitle;

  @Getter
  private final Map<String, String> headers = new LinkedHashMap<>();

  @Setter
  private String message = "Unknown error";

  @Getter
  @Setter
  private String reason;

  @Setter
  private boolean report = true;

  @Getter
  @Setter
  private PathResponseStatus status = PathResponseStatus.INTERNAL_ERROR;

  @Getter
  @Setter
  private String userMessage;

  public PathRequestException() {
    this("Unknown error");
  }

  public PathRequestException(String message) {
    super(message);
    setMessage(message);
  }

  public PathRequestException(Throwable cause) {
    super(cause);
  }

  public PathRequestException(String message, Throwable cause) {
    super(message, cause);
    setMessage(message);
  }

  /**
   * Set message
   * <p>
   *   Note: {@link PathRequestException} overrides base exception message to allow it to be overridden.
   * </p>
   * @return
   */
  @Override
  public String getMessage() {
    return this.message;
  }

  /**
   * Used to append MDX API error code to exception
   *
   * @param newCode - String api error code
   * @return - AccessorException with newCode
   */
  public final PathRequestException withCode(String newCode) {
    setCode(newCode);

    return this;
  }

  public final PathRequestException withErrorTitle(String newErrorTitle) {
    setErrorTitle(newErrorTitle);

    return this;
  }

  /**
   * Used to add a header to be sent with error
   *
   * @param name
   * @param value
   * @return
   */
  public final PathRequestException withHeader(String name, String value) {
    this.headers.put(name, value);

    return this;
  }

  /**
   * Used to override message on exception
   *
   * @param newMessage
   * @return
   */
  public final PathRequestException withMessage(String newMessage) {
    this.setMessage(newMessage);

    return this;
  }

  /**
   * Used to append reason to userMessage
   *
   * @param newReason - String newReason for exception (for userMessage)
   * @return - AccessorException with newReason
   */
  public final PathRequestException withReason(String newReason) {
    this.setReason(newReason);

    return this;
  }

  public final PathRequestException withReport(boolean shouldReport) {
    this.setReport(shouldReport);

    return this;
  }

  public final PathRequestException withStatus(PathResponseStatus newStatus) {
    this.setStatus(newStatus);

    return this;
  }

  public final PathRequestException withUserMessage(String newUserMessage) {
    this.setUserMessage(newUserMessage);

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
}
