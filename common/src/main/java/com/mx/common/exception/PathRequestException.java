package com.mx.common.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.mx.common.accessors.AccessorException;
import com.mx.common.accessors.AccessorMethodNotImplementedException;
import com.mx.common.accessors.AccessorSystemException;
import com.mx.common.accessors.AccessorUserException;
import com.mx.common.accessors.BadRequestException;
import com.mx.common.accessors.InvalidDataException;
import com.mx.common.accessors.PathResponseStatus;
import com.mx.common.accessors.RequestPayloadException;
import com.mx.common.accessors.ResourceNotFoundException;
import com.mx.common.accessors.ResponsePayloadException;
import com.mx.common.accessors.UnauthorizedException;
import com.mx.common.connect.CircuitOpenException;
import com.mx.common.connect.ConnectException;
import com.mx.common.connect.ServiceUnavailableException;
import com.mx.common.connect.TimeoutException;
import com.mx.common.connect.TooManyRequestsException;
import com.mx.common.connect.UpstreamErrorException;
import com.mx.common.facility.FacilityException;
import com.mx.common.gateway.BehaviorException;
import com.mx.common.gateway.GatewayException;
import com.mx.common.messaging.MessageError;

/**
 * Base exception for any request-based error. These are errors that occur at any level during a request
 *
 * <p>For Path system startup errors, use {@link PathSystemException}
 *
 * <p>Hierarchy:
 *
 * <pre>
 *   {@link GatewayException} - Thrown on unrecoverable error in Gateway code
 *     {@link BehaviorException} - Thrown on unrecoverable error in behavior code
 *   {@link AccessorException} - Base class for exceptions thrown by accessors
 *     {@link AccessorUserException} - Thrown on user-related errors in accessor code
 *       {@link ResourceNotFoundException} - Thrown when a resource is requested that does not exist
 *       {@link UnauthorizedException} - Thrown when a user attempts an operation when they are not authenticated, the session is expired, or the session is in a bad state
 *       {@link BadRequestException} - Thrown when a request is malformed or missing required data
 *       {@link InvalidDataException} - Thrown when a request is correctly formed, but the data is invalid for some reason
 *     {@link AccessorSystemException} - Thrown on unrecoverable error in accessor code
 *       {@link AccessorMethodNotImplementedException} - Thrown when an accessor method is invoked that has no implementation
 *       {@link RequestPayloadException} - Thrown when an upstream request payload cannot be built
 *       {@link ResponsePayloadException} - Thrown when an upstream response payload is unrecognizable or un-processable
 *   {@link ConnectException} - Thrown on error in connection code
 *     {@link TimeoutException} - Thrown on a connection/request timeout
 *     {@link ServiceUnavailableException} - Thrown when a service (typically upstream service) is unavailable. The unavailability could be determined by a response from the service or something on the client side
 *       {@link CircuitOpenException} - Thrown on open circuit condition
 *     {@link TooManyRequestsException} - Thrown on open circuit condition
 *     {@link UpstreamErrorException} - Thrown when an unexpected response status is returned from an upstream system call
 *   {@link FacilityException} - Thrown on unrecoverable error in facility code
 *   {@link MessageError} - All errors from messaging system should be wrapped in this exception
 * </pre>
 *
 * <p>Each exception sets its own default Report and Status attributes. Most have constructors that allow setting
 * fields common for their type. {@link PathRequestException} provides fluent setters for all attributes that will
 * allow defaults to be overridden or additional attributes to be set.
 *
 * <p>Example:
 *
 * <pre>{@code
 *     throw new AccessorUserException("Identity unknown", "Username and/or password did not match system", PathResponseStatus.UNAUTHORIZED)
 *       .withReason("Bad password")
 *       .withCode("1401");
 * }</pre>
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
   *
   * <p>Note: {@link PathRequestException} overrides base exception message to allow it to be overridden.
   *
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
