package com.mx.accessors;

import javax.annotation.Nullable;

/**
 * Status codes for accessor responses.
 *
 * Values loosely map to HttpStatus codes.
 * todo: Move back to gateway after model extraction
 */
@SuppressWarnings("checkstyle:magicnumber")
public enum AccessorResponseStatus {

  OK(200, "Successful", false),

  ACCEPTED(202, "Accepted", false),

  NO_CONTENT(204, "No response context", false),

  UNAUTHORIZED(401, "Unauthorized", true),

  NOT_ALLOWED(403, "Operation not allowed", true),

  NOT_FOUND(404, "Resource not found", true),

  USER_ERROR(422, "User-correctable error", true),

  INTERNAL_ERROR(500, "Internal error", true),

  NOT_IMPLEMENTED(501, "API not implemented", true),

  SERVICE_UNAVAILABLE(503, "Service unavailable", true),

  GATEWAY_TIMEOUT(504, "Gateway timeout", true);

  private final String description;

  private final boolean error;

  private final int value;

  AccessorResponseStatus(int value, String description, boolean error) {
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
   * @see AccessorResponseStatus.Series
   */
  public AccessorResponseStatus.Series series() {
    return AccessorResponseStatus.Series.valueOf(this);
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
  public static AccessorResponseStatus valueOf(int statusCode) {
    AccessorResponseStatus status = resolve(statusCode);
    if (status == null) {
      throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
    }
    return status;
  }

  /**
   * Resolve the given status code to an {@code AccessorResponseStatus}, if possible.
   * @param statusCode the HTTP status code (potentially non-standard)
   * @return the corresponding {@code AccessorResponseStatus}, or {@code null} if not found
   * @since 5.0
   */
  @Nullable
  public static AccessorResponseStatus resolve(int statusCode) {
    for (AccessorResponseStatus status : values()) {
      if (status.value == statusCode) {
        return status;
      }
    }
    return null;
  }

  /**
   * Enumeration of HTTP status series.
   * <p>Retrievable via {@link AccessorResponseStatus#series()}.
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
    public static AccessorResponseStatus.Series valueOf(AccessorResponseStatus status) {
      return valueOf(status.value);
    }

    /**
     * Return the enum constant of this type with the corresponding series.
     * @param statusCode the HTTP status code (potentially non-standard)
     * @return the enum constant of this type with the corresponding series
     * @throws IllegalArgumentException if this enum has no corresponding constant
     */
    public static AccessorResponseStatus.Series valueOf(int statusCode) {
      AccessorResponseStatus.Series series = resolve(statusCode);
      if (series == null) {
        throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
      }
      return series;
    }

    /**
     * Resolve the given status code to an {@code AccessorResponseStatus.Series}, if possible.
     * @param statusCode the HTTP status code (potentially non-standard)
     * @return the corresponding {@code Series}, or {@code null} if not found
     * @since 5.1.3
     */
    @Nullable
    public static AccessorResponseStatus.Series resolve(int statusCode) {
      int seriesCode = statusCode / 100;
      for (AccessorResponseStatus.Series series : values()) {
        if (series.value == seriesCode) {
          return series;
        }
      }
      return null;
    }
  }

}
