package com.mx.common.messaging;

/**
 * All errors from messaging system should be wrapped in this exception
 */
public class MessageError extends RuntimeException {
  private MessageStatus messageStatus;

  public MessageError(String message, Throwable cause) {
    super(message, cause);
    this.messageStatus = MessageStatus.FAIL;
  }

  public MessageError(String message, MessageStatus messageStatus, Throwable cause) {
    super(message, cause);
    this.messageStatus = messageStatus;
  }

  public final MessageStatus getMessageStatus() {
    return messageStatus;
  }
}
