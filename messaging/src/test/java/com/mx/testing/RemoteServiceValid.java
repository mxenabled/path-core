package com.mx.testing;

import com.mx.path.connect.messaging.MessageEvent;
import com.mx.path.connect.messaging.MessageRequest;
import com.mx.path.connect.messaging.MessageResponse;
import com.mx.path.connect.messaging.remote.Listener;
import com.mx.path.connect.messaging.remote.RemoteService;
import com.mx.path.connect.messaging.remote.Responder;
import com.mx.path.core.common.messaging.MessageStatus;

public class RemoteServiceValid extends RemoteService<RemoteAccount> {
  public RemoteServiceValid(String clientId) {
    super(clientId);
  }

  @Responder
  public MessageResponse list(String channel, MessageRequest messageRequest) {
    return MessageResponse.builder().status(MessageStatus.SUCCESS).build();
  }

  @Listener
  public void changed(String channel, MessageEvent messageEvent) {
  }
}
