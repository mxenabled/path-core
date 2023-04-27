package com.mx.path.core.common.messaging;

import lombok.Getter;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * All errors from messaging system should be wrapped in this exception
 */
public class MessageError extends PathRequestException {
  @Getter
  private final MessageStatus messageStatus;

  public MessageError(String message, Throwable cause) {
    super(message, cause);
    setReport(true);
    this.messageStatus = MessageStatus.FAIL;
  }

  public MessageError(String message, MessageStatus messageStatus, Throwable cause) {
    super(message, cause);
    setReport(true);
    this.messageStatus = messageStatus;
  }
}
