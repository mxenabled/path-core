package com.mx.path.core.common.messaging;

import com.mx.path.core.common.http.HttpStatus;

/**
 * Response statuses
 */
public enum MessageStatus {

  SUCCESS(200, "OK"),                      // Responder succeeded
  CREATED(201, "Created"),
  ACCEPTED(202, "Accepted"),

  NOT_AUTHORIZED(401, "Unauthorized"),     // Not authorized to perform action
  NOT_FOUND(404, "Not Found"),             // Resource not found
  NO_RESPONDER(405, "Method Not Allowed"), // No responder implementation is found
  TIMEOUT(408, "Remote request timeout"),  // Responder did not respond in time

  FAIL(500, "Remote call failed"),         // Responder raised an error
  INVALID_RESPONDER(502, "Bad Gateway"),   // Responder not implemented correctly
  DISABLED(503, "Service Unavailable");    // Messaging is not enabled

  public static MessageStatus resolve(int statusCode) {
    for (MessageStatus status : values()) {
      if (status.value == statusCode) {
        return status;
      }
    }

    return null;
  }

  public static MessageStatus fromHttpStatus(HttpStatus httpStatus) {
    if (httpStatus != null) {
      return resolve(httpStatus.value());
    }

    return null;
  }

  private final int value;
  private final String reasonPhrase;

  MessageStatus(int value, String reasonPhrase) {
    this.value = value;
    this.reasonPhrase = reasonPhrase;
  }

  /**
   * Return the integer value of this status code.
   */
  public int value() {
    return this.value;
  }

  /**
   * Return the reason phrase of this status code.
   */
  public String getReasonPhrase() {
    return this.reasonPhrase;
  }

  public HttpStatus toHttpStatus() {
    return HttpStatus.resolve(value());
  }

}
