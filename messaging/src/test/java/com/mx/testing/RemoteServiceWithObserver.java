package com.mx.testing;

import com.mx.path.connect.messaging.MessageEvent;
import com.mx.path.connect.messaging.MessageRequest;
import com.mx.path.connect.messaging.MessageResponse;
import com.mx.path.connect.messaging.remote.RemoteService;
import com.mx.path.core.common.messaging.EventListener;
import com.mx.path.core.common.messaging.MessageResponder;
import com.mx.path.core.common.messaging.MessageStatus;

public class RemoteServiceWithObserver extends RemoteService<RemoteAccount> {

  public RemoteServiceWithObserver(String clientId) {
    super(clientId);
  }

  private MessageResponder mockTestObserverMessageResponder;
  private EventListener mockTestObserverEventListener;

  /**
   * This is an mockTestObserver test listener
   * @param mockTestObserverMessageResponder
   */
  public void setMockTestObserverMessageResponder(MessageResponder mockTestObserverMessageResponder) {
    this.mockTestObserverMessageResponder = mockTestObserverMessageResponder;
  }

  /**
   * This is an mockTestObserver test listener
   * @param mockTestObserverEventListener
   */
  public void setMockTestObserverEventListener(EventListener mockTestObserverEventListener) {
    this.mockTestObserverEventListener = mockTestObserverEventListener;
  }

  @Override
  public MessageResponse dispatch(String channel, MessageRequest messageRequest) {
    mockTestObserverMessageResponder.respond(channel, messageRequest.toJson());
    return MessageResponse.builder().status(MessageStatus.SUCCESS).build();
  }

  @Override
  public void dispatch(String channel, MessageEvent messageEvent) {
    this.mockTestObserverEventListener.receive(channel, messageEvent.toJson());
  }

  public MessageResponse list(String channel, MessageRequest messageRequest) {
    return MessageResponse.builder().status(MessageStatus.SUCCESS).build();
  }
}
