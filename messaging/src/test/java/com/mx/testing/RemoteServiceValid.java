package com.mx.testing;

import com.mx.common.messaging.MessageStatus;
import com.mx.path.api.connect.messaging.MessageEvent;
import com.mx.path.api.connect.messaging.MessageRequest;
import com.mx.path.api.connect.messaging.MessageResponse;
import com.mx.path.api.connect.messaging.remote.Listener;
import com.mx.path.api.connect.messaging.remote.RemoteService;
import com.mx.path.api.connect.messaging.remote.Responder;
import com.mx.path.api.connect.messaging.remote.models.RemoteAccount;

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
