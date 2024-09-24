package com.mx.path.core.common.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.accessor.AccessorException;
import com.mx.path.core.common.accessor.AccessorMethodNotImplementedException;
import com.mx.path.core.common.accessor.AccessorSystemException;
import com.mx.path.core.common.accessor.AccessorUserException;
import com.mx.path.core.common.accessor.BadRequestException;
import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.accessor.RequestPayloadException;
import com.mx.path.core.common.accessor.RequestValidationException;
import com.mx.path.core.common.accessor.ResourceNotFoundException;
import com.mx.path.core.common.accessor.ResponsePayloadException;
import com.mx.path.core.common.accessor.UnauthorizedException;
import com.mx.path.core.common.accessor.UpstreamSystemMaintenance;
import com.mx.path.core.common.accessor.UpstreamSystemUnavailable;
import com.mx.path.core.common.connect.CircuitOpenException;
import com.mx.path.core.common.connect.ConnectException;
import com.mx.path.core.common.connect.ServiceUnavailableException;
import com.mx.path.core.common.connect.TimeoutException;
import com.mx.path.core.common.connect.TooManyRequestsException;
import com.mx.path.core.common.connect.UpstreamErrorException;
import com.mx.path.core.common.facility.FacilityException;
import com.mx.path.core.common.gateway.BehaviorException;
import com.mx.path.core.common.gateway.GatewayException;
import com.mx.path.core.common.messaging.MessageError;

/**
 * Base exception for any request-based error. These are errors that occur at any level during a request:
 *
 * <p>For Path system startup errors, use {@link PathSystemException}
 *
 * <p>Hierarchy:
 *
 * <pre>
 *   {@link GatewayException} - Thrown on unrecoverable error in Gateway code
 *     {@link BehaviorException} - Thrown on unrecoverable error in behavior code
 *   {@link AccessorException} - Base class for exceptions thrown by accessors
 *     {@link AccessorUserException} - Base class for exceptions thrown on user-related errors in accessor code
 *       {@link ResourceNotFoundException} - Thrown when a resource is requested that does not exist
 *       {@link UnauthorizedException} - Thrown when a user attempts an operation when they are not authenticated, the session is expired, or the session is in a bad state
 *       {@link BadRequestException} - Thrown when a request is malformed or missing required data
 *       {@link RequestValidationException} - Thrown when a request is correctly formed, but the data is invalid for some reason
 *     {@link AccessorSystemException} - Base class for exceptions thrown on unrecoverable error in accessor code
 *       {@link AccessorMethodNotImplementedException} - Thrown when an accessor method is invoked that has no implementation
 *       {@link RequestPayloadException} - Thrown when an upstream request payload cannot be built
 *       {@link ResponsePayloadException} - Thrown when an upstream response payload is unrecognizable or un-processable
 *       {@link UpstreamSystemUnavailable} - Thrown when an upstream request results in an unknown error or the upstream service is determined to be offline
 *         {@link UpstreamSystemMaintenance} - Thrown when the upstream system is determined to be offline due to maintenance
 *   {@link ConnectException} - Thrown on error in connection code
 *     {@link TimeoutException} - Thrown on a connection/request timeout
 *     {@link ServiceUnavailableException} - Thrown when a service (typically upstream service) is unavailable. The unavailability could be determined by a response from the service or something on the client side
 *       {@link CircuitOpenException} - Thrown on open circuit condition
 *     {@link TooManyRequestsException} - Thrown when bulkhead rejects call due to too many requests
 *     {@link UpstreamErrorException} - Thrown when an unexpected response status is returned from an upstream system call
 *   {@link FacilityException} - Thrown on unrecoverable error in facility code
 *   {@link MessageError} - All errors from messaging system should be wrapped in this exception
 *   {@link PathRequestExceptionWrapper} - Wraps a cause so that other attributes can be added. This exception's cause should be inspected.
 * </pre>
 *
 * <p>Each exception sets its own default Report, IsInternal, and Status attributes. Most have constructors that allow setting
 * fields common for their type. {@link PathRequestException} provides fluent setters for all attributes that will
 * allow defaults to be overridden or additional attributes to be set.
 *
 * <p>Example:
 *
 * <pre>{@code
 *   public class FailedLoginException extends AccessorUserException {
 *     public FailedLoginException(String message, String userMessage) {
 *       super(message);
 *       setStatus(PathResponseStatus.UNAUTHORIZED);
 *     }
 *   }
 *
 *   throw new FailedLoginException("Identity unknown", "Username and/or password did not match system")
 *     .withCode("1401");
 * }</pre>
 */
public abstract class PathRequestException extends RuntimeException {

  /**
   * Exception code.
   *
   * -- GETTER --
   * Return code.
   *
   * @return code
   * -- SETTER --
   * Set code.
   *
   * @param code code to set
   */
  @Getter
  @Setter
  private String code;

  /**
   * Title for exception modal screen.
   *
   * -- GETTER --
   * Return title.
   *
   * @return title
   * -- SETTER --
   * Set title.
   *
   * @param errorTitle title to set
   */
  @Getter
  @Setter
  private String errorTitle;

  private final Map<String, String> headers = new LinkedHashMap<>();

  /**
   * Indicates whether this exception is internal to the system or occurred upstream.
   *
   * -- GETTER --
   * Return is internal.
   *
   * @return is internal
   * -- SETTER --
   * Set is internal.
   *
   * @param internal is internal value to set
   */
  @Getter
  @Setter
  private boolean internal = false;

  /**
   * Description message (not prompted to user).
   *
   * -- SETTER --
   * Set message.
   *
   * @param message message to set
   */
  @Setter
  private String message = "Unknown error";

  /**
   * Reason for exception.
   *
   * -- GETTER --
   * Return reason.
   *
   * @return reason
   * -- SETTER --
   * Set reason.
   *
   * @param reason reason to set
   */
  @Getter
  @Setter
  private String reason;

  /**
   * -- SETTER --
   * Set report.
   *
   * @param report report to set
   */
  @Setter
  private boolean report = true;

  /**
   * Path response status.
   *
   * -- GETTER --
   * Return status.
   *
   * @return status
   * -- SETTER --
   * Set status.
   *
   * @param status status to set
   */
  @Getter
  @Setter
  private PathResponseStatus status = PathResponseStatus.INTERNAL_ERROR;

  /**
   * Exception description message prompted to user.
   *
   * -- GETTER --
   * Return user message.
   *
   * @return user message
   * -- SETTER --
   * Set user message.
   *
   * @param userMessage user message to set
   */
  @Getter
  @Setter
  private String userMessage;

  /**
   * Default constructor.
   */
  public PathRequestException() {
    this("Unknown error");
  }

  /**
   * Build new {@link PathRequestException} with specified parameters.
   *
   * @param message message
   */
  public PathRequestException(String message) {
    super(message);
    setMessage(message);
  }

  /**
   * Build new {@link PathRequestException} with specified parameters.
   *
   * @param cause cause
   */
  public PathRequestException(Throwable cause) {
    super(cause);
  }

  /**
   * Build new {@link PathRequestException} with specified parameters.
   *
   * @param message message
   * @param cause cause
   */
  public PathRequestException(String message, Throwable cause) {
    super(message, cause);
    setMessage(message);
  }

  /**
   * Return headers.
   *
   * @return headers
   */
  public final Map<String, String> getHeaders() {
    if (internal) {
      headers.put("X-Internal-Error", "true");
    } else {
      headers.remove("X-Internal-Error");
    }

    return headers;
  }

  /**
   * Return message.
   *
   * <p>Note: {@link PathRequestException} overrides base exception message to allow it to be overridden.
   *
   * @return message
   */
  @Override
  public String getMessage() {
    return this.message;
  }

  /**
   * Used to append MDX API error code to exception.
   *
   * @param newCode - String api error code
   * @return - AccessorException with newCode
   * @param <T> exception type
   */
  @SuppressWarnings("unchecked")
  public final <T extends PathRequestException> T withCode(String newCode) {
    setCode(newCode);

    return (T) this;
  }

  /**
   * Used to append title to exception.
   *
   * @param newErrorTitle title
   * @return AccessorException with new title
   * @param <T> exception type
   */
  @SuppressWarnings("unchecked")
  public final <T extends PathRequestException> T withErrorTitle(String newErrorTitle) {
    setErrorTitle(newErrorTitle);

    return (T) this;
  }

  /**
   * Used to add a header to be sent with error.
   *
   * @param name name
   * @param value value
   * @return self
   * @param <T> exception type
   */
  @SuppressWarnings("unchecked")
  public final <T extends PathRequestException> T withHeader(String name, String value) {
    this.headers.put(name, value);

    return (T) this;
  }

  /**
   * Used to append is internal parameter to exception.
   *
   * @param isInternal is internal
   * @return self
   * @param <T> exception type
   */
  @SuppressWarnings("unchecked")
  public final <T extends PathRequestException> T withIsInternal(boolean isInternal) {
    this.internal = isInternal;

    return (T) this;
  }

  /**
   * Used to override message on exception.
   *
   * @param newMessage new message
   * @return self
   * @param <T> exception type
   */
  @SuppressWarnings("unchecked")
  public final <T extends PathRequestException> T withMessage(String newMessage) {
    this.setMessage(newMessage);

    return (T) this;
  }

  /**
   * Used to append reason to userMessage.
   *
   * @param newReason string newReason for exception (for userMessage)
   * @return AccessorException with newReason
   * @param <T> exception type
   */
  @SuppressWarnings("unchecked")
  public final <T extends PathRequestException> T withReason(String newReason) {
    this.setReason(newReason);

    return (T) this;
  }

  /**
   * Used to set should report parameter to exception.
   *
   * @param shouldReport should report
   * @return self
   * @param <T> exception type
   */
  @SuppressWarnings("unchecked")
  public final <T extends PathRequestException> T withReport(boolean shouldReport) {
    this.setReport(shouldReport);

    return (T) this;
  }

  /**
   * Used to set status on exception.
   *
   * @param newStatus new status
   * @return self
   * @param <T> exception type
   */
  @SuppressWarnings("unchecked")
  public final <T extends PathRequestException> T withStatus(PathResponseStatus newStatus) {
    this.setStatus(newStatus);

    return (T) this;
  }

  /**
   * Used to set user message to exception.
   *
   * @param newUserMessage user message
   * @return self
   * @param <T> exception type
   */
  @SuppressWarnings("unchecked")
  public final <T extends PathRequestException> T withUserMessage(String newUserMessage) {
    this.setUserMessage(newUserMessage);

    return (T) this;
  }

  /**
   * Report accessor fct for boolean.
   *
   * @return boolean report
   */
  public final boolean shouldReport() {
    return report;
  }
}
