package com.mx.path.core.common.messaging;

import com.mx.path.core.common.http.HttpStatus;

/**
 * Response statuses.
 */
public enum MessageStatus {

  /**
   * Responder succeeded.
   */
  SUCCESS(200, "OK"),
  /**
   * Resource successfully created.
   */
  CREATED(201, "Created"),
  /**
   * Request accepted for processing but not completed.
   */
  ACCEPTED(202, "Accepted"),
  /**
   * Not authorized to perform action.
   */
  NOT_AUTHORIZED(401, "Unauthorized"),
  /**
   * Resource not found.
   */
  NOT_FOUND(404, "Not Found"),
  /**
   * No implementation found for requested action.
   */
  NO_RESPONDER(405, "Method Not Allowed"),
  /**
   * Responder did not respond in time.
   */
  TIMEOUT(408, "Remote request timeout"),
  /**
   * Responder raised an error.
   */
  FAIL(500, "Remote call failed"),
  /**
   * Responder not implemented correctly.
   */
  INVALID_RESPONDER(502, "Bad Gateway"),
  /**
   * Service is currently disabled or unavailable.
   */
  DISABLED(503, "Service Unavailable");

  /**
   * Resolve {@link MessageStatus} based on status code.
   *
   * @param statusCode status to resolve
   * @return resolved MessageStatus, null if no match found
   */
  public static MessageStatus resolve(int statusCode) {
    for (MessageStatus status : values()) {
      if (status.value == statusCode) {
        return status;
      }
    }

    return null;
  }

  /**
   * Resolve {@link MessageStatus} based on {@link HttpStatus}.
   *
   * @param httpStatus status to resolve
   * @return resolved MessageStatus, null if no match found
   */
  public static MessageStatus fromHttpStatus(HttpStatus httpStatus) {
    if (httpStatus != null) {
      return resolve(httpStatus.value());
    }

    return null;
  }

  private final int value;
  private final String reasonPhrase;

  /**
   * Build new {@link MessageStatus} instance based on specified parameters.
   *
   * @param value status code
   * @param reasonPhrase reason
   */
  MessageStatus(int value, String reasonPhrase) {
    this.value = value;
    this.reasonPhrase = reasonPhrase;
  }

  /**
   * Return the integer value of this status code.
   *
   * @return interger associated value
   */
  public int value() {
    return this.value;
  }

  /**
   * Return the reason phrase of this status code.
   *
   * @return reason phrase
   */
  public String getReasonPhrase() {
    return this.reasonPhrase;
  }

  /**
   * Resolve this instance to {@link HttpStatus}.
   *
   * @return http status
   */
  public HttpStatus toHttpStatus() {
    return HttpStatus.resolve(value());
  }

}
