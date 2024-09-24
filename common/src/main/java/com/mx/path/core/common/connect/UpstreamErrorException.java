package com.mx.path.core.common.connect;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.accessor.AccessorUserException;
import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.http.HttpStatus;

/**
 * Thrown when an unexpected response status is returned from an upstream system call.
 *
 * <p>Note: This is not a typical exception. It is not good form to bubble up response statuses from upstream calls
 * The status should be translated to the appropriate {@link PathResponseStatus} and raised in an appropriate
 * exception, like {@link AccessorUserException}.
 */
public class UpstreamErrorException extends ConnectException {

  /**
   * -- GETTER --
   * Return original status.
   *
   * @return original status
   * -- SETTER --
   * Set original status.
   *
   * @param originalStatus original status to set
   */
  @Setter
  @Getter
  private HttpStatus originalStatus;

  /**
   * Build new {@link UpstreamErrorException} with specified parameters.
   *
   * @param message message
   * @param originalStatus original status
   * @param status status
   */
  public UpstreamErrorException(String message, HttpStatus originalStatus, PathResponseStatus status) {
    super(message, status);
    setOriginalStatus(originalStatus);
    setReport(false);
  }

  /**
   * Build new {@link UpstreamErrorException} with specified parameters.
   *
   * @param message message
   * @param originalStatus original status
   * @param status status
   * @param cause cause
   */
  public UpstreamErrorException(String message, HttpStatus originalStatus, PathResponseStatus status, Throwable cause) {
    super(message, status, cause);
    setOriginalStatus(originalStatus);
    setReport(false);
  }
}
