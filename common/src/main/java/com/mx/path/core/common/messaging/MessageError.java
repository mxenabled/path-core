package com.mx.path.core.common.messaging;

import lombok.Getter;

import com.mx.path.core.common.exception.PathRequestException;

/**
 * All errors from messaging system should be wrapped in this exception.
 */
public class MessageError extends PathRequestException {

  /**
   * -- GETTER --
   * Return message status.
   *
   * @return message status
   */
  @Getter
  private final MessageStatus messageStatus;

  /**
   * Create new {@link MessageError} with specified parameters.
   *
   * @param message message
   * @param cause cause
   */
  public MessageError(String message, Throwable cause) {
    super(message, cause);
    setReport(true);
    this.messageStatus = MessageStatus.FAIL;
  }

  /**
   * Create new {@link MessageError} with specified parameters.
   *
   * @param message message
   * @param messageStatus status
   * @param cause cause
   */
  public MessageError(String message, MessageStatus messageStatus, Throwable cause) {
    super(message, cause);
    setReport(true);
    this.messageStatus = messageStatus;
  }
}
