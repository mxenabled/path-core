package com.mx.common.accessors;

import javax.annotation.Nullable;

import com.mx.common.http.HttpStatus;

/**
 * Status codes for Path responses.
 *
 * <p>These statuses map loosely to HTTP status. Some of the names have been changed to clarify their
 * intended use in Path.
 */
@SuppressWarnings("checkstyle:magicnumber")
public enum PathResponseStatus {

  /**
   * Request successful with a response body
   */
  OK(200, "Successful", false),

  /**
   * Request successful with a challenge response body
   */
  ACCEPTED(202, "Accepted", false),

  /**
   * Request successful with no response body
   */
  NO_CONTENT(204, "No response context", false),

  /**
   * The request body is incorrect or failed validation
   */
  BAD_REQUEST(400, "Bad request", true),

  /**
   * The request not allowed because user is not authenticated, the session is no longer valid
   */
  UNAUTHORIZED(401, "Unauthorized", true),

  /**
   * The current user is not allowed to perform the requested operation
   */
  NOT_ALLOWED(403, "Operation not allowed", true),

  /**
   * The requested resource does not exist or does not belong to the user
   * <p>This should <i>not</i> be used in the case a list is requested and the list is empty. In that case,
   * the response status should be OK with an empty list.
   */
  NOT_FOUND(404, "Resource not found", true),

  /**
   * The request cannot be performed because of some user-correctable reason.
   *
   * Example reasons:
   * <ul>
   *   <li>Transfer amount too large, because not enough money in source account. Please decrease the amount or choose a different account</li>
   *   <li>Payment amount is greater than allowed amount. Please decrease the amount to below $500</li>
   *   <li>Remote Deposit cannot be completed because the user has not signed up for Remote Deposits. Please visit the online banking site to sign up.</li>
   *   <li>User cannot log in because the account is not properly provisioned. Please call customer service to get the problem corrected.</li>
   * </ul>
   */
  USER_ERROR(422, "User-correctable error", true),

  /**
   * The request encountered an upstream system that has too many requests in configured window. Not typically used directly.
   */
  TOO_MANY_REQUESTS(429, "Too many requests", true),

  /**
   * Something went wrong while processing request.
   *
   * <p>This should be reserved for situations that can only be corrected by the developer. In other words,
   *   the code or environment are broken.
   *
   * Examples:
   * <ul>
   *   <li>The configured certificate and private key do not match</li>
   *   <li>A JWT could not be parsed</li>
   *   <li>Failed to store value in key-value-store due to network failure</li>
   *   <li>Got a null value, where a non-null was expected (not user-provided)</li>
   * </ul>
   */
  INTERNAL_ERROR(500, "Internal error", true),

  /**
   * Indicates a method was invoked that has not been implemented. Not typically used directly.
   */
  NOT_IMPLEMENTED(501, "API not implemented", true),

  /**
   * System is unavailable and unable to process request. Not typically used directly.
   * <p>This is used to indicate an open circuit.
   */
  UNAVAILABLE(503, "Service unavailable", true),

  /**
   * A request failed to respond in less than expected time. Not typically used directly.
   */
  TIMEOUT(504, "Timeout", true),

  /**
   * The upstream service returned an unexpected error or is determined to be offline.
   */
  UPSTREAM_SERVICE_UNAVAILABLE(531, "Upstream service unavailable", true);

  private final String description;

  private final boolean error;

  private final int value;

  PathResponseStatus(int value, String description, boolean error) {
    this.description = description;
    this.error = error;
    this.value = value;
  }

  /**
   * Return the integer value of this status code.
   */
  public int value() {
    return this.value;
  }

  /**
   * Return the integer value of this status code.
   */
  public boolean isError() {
    return this.error;
  }

  /**
   * Return the reason phrase of this status code.
   */
  public String getReasonPhrase() {
    return this.description;
  }

  /**
   * Return the HTTP status series of this status code.
   * @see PathResponseStatus.Series
   */
  public PathResponseStatus.Series series() {
    return PathResponseStatus.Series.valueOf(this);
  }

  /**
   * @return HttpStatus equivalent
   */
  public HttpStatus toHttpStatus() {
    return HttpStatus.resolve(value);
  }

  /**
   * Return a string representation of this status code.
   */
  @Override
  public String toString() {
    return this.value + " " + name();
  }

  /**
   * Return the enum constant of this type with the specified numeric value.
   * @param statusCode the numeric value of the enum to be returned
   * @return the enum constant with the specified numeric value
   * @throws IllegalArgumentException if this enum has no constant for the specified numeric value
   */
  public static PathResponseStatus valueOf(int statusCode) {
    PathResponseStatus status = resolve(statusCode);
    if (status == null) {
      throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
    }
    return status;
  }

  /**
   * Resolve the given status code to an {@code PathResponseStatus}, if possible.
   * @param statusCode the HTTP status code (potentially non-standard)
   * @return the corresponding {@code PathResponseStatus}, or {@code null} if not found
   * @since 5.0
   */
  @Nullable
  public static PathResponseStatus resolve(int statusCode) {
    for (PathResponseStatus status : values()) {
      if (status.value == statusCode) {
        return status;
      }
    }
    return null;
  }

  /**
   * Enumeration of HTTP status series.
   * <p>Retrievable via {@link PathResponseStatus#series()}.
   */
  public enum Series {

    INFORMATIONAL(1), SUCCESSFUL(2), REDIRECTION(3), CLIENT_ERROR(4), SERVER_ERROR(5);

    private final int value;

    Series(int value) {
      this.value = value;
    }

    /**
     * Return the integer value of this status series. Ranges from 1 to 5.
     */
    public int value() {
      return this.value;
    }

    /**
     * Return the enum constant of this type with the corresponding series.
     * @param status a standard HTTP status enum value
     * @return the enum constant of this type with the corresponding series
     * @throws IllegalArgumentException if this enum has no corresponding constant
     */
    public static PathResponseStatus.Series valueOf(PathResponseStatus status) {
      return valueOf(status.value);
    }

    /**
     * Return the enum constant of this type with the corresponding series.
     * @param statusCode the HTTP status code (potentially non-standard)
     * @return the enum constant of this type with the corresponding series
     * @throws IllegalArgumentException if this enum has no corresponding constant
     */
    public static PathResponseStatus.Series valueOf(int statusCode) {
      PathResponseStatus.Series series = resolve(statusCode);
      if (series == null) {
        throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
      }
      return series;
    }

    /**
     * Resolve the given status code to an {@code PathResponseStatus.Series}, if possible.
     * @param statusCode the HTTP status code (potentially non-standard)
     * @return the corresponding {@code Series}, or {@code null} if not found
     * @since 5.1.3
     */
    @Nullable
    public static PathResponseStatus.Series resolve(int statusCode) {
      int seriesCode = statusCode / 100;
      for (PathResponseStatus.Series series : values()) {
        if (series.value == seriesCode) {
          return series;
        }
      }
      return null;
    }
  }
}
