package com.mx.common.messaging;

import lombok.Getter;
import lombok.Setter;

import com.mx.common.exception.PathRequestException;

/**
 * Exception types not recognized by projects calling the messaging system will be returned as a RemoteException
 */
public class RemoteException extends PathRequestException {

  @Getter
  @Setter
  private String originalType = null;

  public RemoteException(String message, Throwable cause) {
    super(message, cause);
  }
}
