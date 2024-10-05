package com.mx.path.core.common.messaging;

import com.mx.path.core.common.accessor.PathResponseStatus;
import com.mx.path.core.common.exception.PathRequestException;

/**
 * All errors from NATS should be wrapped in this exception
 */
public class MessageBrokerException extends PathRequestException {

  public MessageBrokerException(String message, Throwable cause) {
    super(message, cause);
    setInternal(true);
    setReport(true);
    setStatus(PathResponseStatus.INTERNAL_ERROR);
  }
}
