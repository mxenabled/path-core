package com.mx.testing;

import com.mx.common.messaging.MessageStatus;
import com.mx.path.api.connect.messaging.MessageEvent;
import com.mx.path.api.connect.messaging.MessageRequest;
import com.mx.path.api.connect.messaging.MessageResponse;
import com.mx.path.api.connect.messaging.remote.Listener;
import com.mx.path.api.connect.messaging.remote.RemoteService;
import com.mx.path.api.connect.messaging.remote.Responder;

public class RemoteServiceWithInvalid extends RemoteService<RemoteAccount> {
  public RemoteServiceWithInvalid(String clientId) {
    super(clientId);
  }

  // This is a valid responder method
  @Responder
  public MessageResponse list(String channel, MessageRequest messageRequest) {
    return MessageResponse.builder().status(MessageStatus.SUCCESS).build();
  }

  // This is an invalid responder method (wrong parameters)
  @Responder
  public MessageResponse wrongArgs(MessageRequest messageRequest) {
    return MessageResponse.builder().status(MessageStatus.SUCCESS).build();
  }

  // This is a invalid responder method (wrong return type)
  @Responder
  public String invalid(String channel, MessageRequest messageResponse) {
    return MessageResponse.builder().status(MessageStatus.SUCCESS).build().toJson();
  }

  // This is a valid event receiver method
  @Listener
  public void changed(String channel, MessageEvent messageEvent) {
  }

  // This is a valid event receiver method
  @Listener
  public void bad(MessageEvent messageEvent) {
  }
}
