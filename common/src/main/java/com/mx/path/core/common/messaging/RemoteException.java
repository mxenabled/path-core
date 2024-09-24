package com.mx.path.core.common.messaging;

import lombok.Getter;
import lombok.Setter;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * Exception types not recognized by projects calling the messaging system will be returned as a RemoteException
 */
public class RemoteException extends PathRequestException {

  /**
   * -- GETTER --
   * Return original exception type.
   *
   * @return original exception type
   * -- SETTER --
   * Set original exception type.
   *
   * @param originalType original exception type to set
   */
  @Getter
  @Setter
  private String originalType = null;

  /**
   * Build new {@link RemoteException} with specified message and cause.
   *
   * @param message message
   * @param cause cause
   */
  public RemoteException(String message, Throwable cause) {
    super(message, cause);
  }
}
