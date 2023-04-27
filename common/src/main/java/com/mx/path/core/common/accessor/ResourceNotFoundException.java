package com.mx.path.core.common.accessor;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Thrown when a resource is requested that does not exist.
 *
 * <p>Note: This should not be used in the case where a resource list is requested and the result is empty. If the result
 * is empty, just return an empty list.
 *
 * <p>See {@link PathRequestException} for usage details
 */
public class ResourceNotFoundException extends AccessorUserException {
  public ResourceNotFoundException(String message, String userMessage) {
    super(message, userMessage, PathResponseStatus.NOT_FOUND);
  }

  public ResourceNotFoundException(String message, String userMessage, Throwable cause) {
    super(message, userMessage, PathResponseStatus.NOT_FOUND, cause);
  }
}
