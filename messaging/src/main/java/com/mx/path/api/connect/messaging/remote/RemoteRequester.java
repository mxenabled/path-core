package com.mx.path.api.connect.messaging.remote;

import java.lang.reflect.ParameterizedType;
import java.util.function.Function;

import com.mx.common.lang.Strings;
import com.mx.common.messaging.MessageBroker;
import com.mx.common.messaging.MessageError;
import com.mx.common.messaging.MessageStatus;
import com.mx.path.api.connect.messaging.MessageEvent;
import com.mx.path.api.connect.messaging.MessageRequest;
import com.mx.path.api.connect.messaging.MessageResponse;
import com.mx.path.api.connect.messaging.RequestContextHeaderForwarder;
import com.mx.path.model.context.RequestContext;
import com.mx.path.model.context.Session;
import com.mx.path.model.context.facility.Facilities;

/**
 * Service that enables Middleware Services to request data
 */
public abstract class RemoteRequester<T> {
  // Fields

  private final Class<T> classOfT;
  private final RequestContextHeaderForwarder requestContextHeaderForwarder;
  private RemoteLogger logger = new RemoteLogger();

  // Constructor

  @SuppressWarnings("unchecked")
  public RemoteRequester() {
    this.classOfT = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    this.requestContextHeaderForwarder = new RequestContextHeaderForwarder();
  }

  // Public

  /**
   * Request data with messageRequest
   *
   * @param messageRequest
   * @return response
   */
  public final MessageResponse request(String clientId, MessageRequest messageRequest) {
    String responseStr;
    try {
      String channel = RemoteChannel.buildRequestChannel(clientId, classOfT, messageRequest);
      messageRequest.setChannel(channel);
      responseStr = messageBroker(clientId).request(channel, messageRequest.toJson());
    } catch (MessageError e) {

      return MessageResponse.builder()
          .exception(e)
          .error(e.getMessage())
          .status(e.getMessageStatus())
          .request(messageRequest)
          .build();
    } catch (Exception e) {

      return MessageResponse.builder()
          .exception(e)
          .error(e.getMessage())
          .status(MessageStatus.FAIL)
          .request(messageRequest)
          .build();
    }

    try {
      MessageResponse response = MessageResponse.fromJson(responseStr);
      response.setRequest(messageRequest);

      return response;
    } catch (Exception e) {
      return MessageResponse.builder()
          .exception(e)
          .status(MessageStatus.FAIL)
          .request(messageRequest)
          .build();
    }
  }

  /**
   * Emit an event with messageEvent
   *
   * @param clientId
   * @param messageEvent
   */
  public final void send(String clientId, MessageEvent messageEvent) {
    String channel = RemoteChannel.buildEventChannel(clientId, classOfT, messageEvent);
    messageEvent.setChannel(channel);
    messageBroker(clientId).publish(channel, messageEvent.toJson());
  }

  // Protected

  /**
   * @param clientId
   * @param messageRequest to execute
   * @return remote response to request
   */
  protected MessageResponse executeRequest(String clientId, MessageRequest messageRequest) {
    return withSession(messageRequest, messageRequest1 -> {
      return withTracing(messageRequest1, messageRequest2 -> {
        return withTiming(messageRequest2, messageRequest3 -> {
          MessageResponse resp = request(clientId, messageRequest3);
          resp.setRequest(messageRequest3);
          logger.logRequest(resp);

          return resp;
        });
      });
    });
  }

  protected final Class<T> getClassOfT() {
    return classOfT;
  }

  // Package Private

  /**
   * Saves the Session, forwards relevant headers, and re-inflates the Session for the given request.
   *
   * @param messageRequest
   * @param f
   * @return MessageResponse
   */
  MessageResponse withSession(MessageRequest messageRequest, Function<MessageRequest, MessageResponse> f) {
    String sessionId = null;
    if (Session.current() != null) {
      messageRequest.getMessageHeaders().setSessionId(Session.current().getId());
      messageRequest.getMessageParameters().setUserId(Session.current().getUserId());

      sessionId = Session.current().getId();
      Session.current().save(); // We are leaving the process context. Persist the session for use on the other side.
    }

    requestContextHeaderForwarder.injectIntoMessageHeaders(RequestContext.current(), messageRequest.getMessageHeaders());

    MessageResponse response = f.apply(messageRequest);

    // Reinflate the session (in case it was changed on the other side.)
    Session.clearSession();
    if (Strings.isNotBlank(sessionId)) {
      Session.loadSession(sessionId);
    }

    return response;
  }

  // Private

  private MessageResponse withTiming(MessageRequest messageRequest, Function<MessageRequest, MessageResponse> f) {
    MessageResponse response;
    messageRequest.start();
    try {
      response = f.apply(messageRequest);
    } finally {
      messageRequest.finish();
    }

    return response;
  }

  private MessageResponse withTracing(MessageRequest messageRequest, Function<MessageRequest, MessageResponse> f) {
    RemoteTracePropagation.inject(messageRequest);

    return f.apply(messageRequest);
  }

  // Private

  /**
   * @return Retrieves a MessageBroker from Facilities.
   *
   * @param clientId
   */
  private MessageBroker messageBroker(String clientId) {
    MessageBroker messageBroker = Facilities.getMessageBroker(clientId);
    if (messageBroker == null) {
      throw new MessageError("No MessageBroker configured for client: " + clientId, MessageStatus.DISABLED, null);
    }
    return messageBroker;
  }
}
