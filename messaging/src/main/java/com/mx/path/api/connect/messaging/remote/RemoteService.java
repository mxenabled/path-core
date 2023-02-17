package com.mx.path.api.connect.messaging.remote;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.function.Supplier;

import com.mx.common.lang.Strings;
import com.mx.common.messaging.EventListener;
import com.mx.common.messaging.MessageBroker;
import com.mx.common.messaging.MessageError;
import com.mx.common.messaging.MessageResponder;
import com.mx.common.messaging.MessageStatus;
import com.mx.path.api.connect.messaging.FacilityMessageBrokerSupplier;
import com.mx.path.api.connect.messaging.MessageEvent;
import com.mx.path.api.connect.messaging.MessageHeaders;
import com.mx.path.api.connect.messaging.MessageRequest;
import com.mx.path.api.connect.messaging.MessageResponse;
import com.mx.path.api.connect.messaging.RequestContextHeaderForwarder;
import com.mx.path.model.context.RequestContext;
import com.mx.path.model.context.Session;
import com.mx.path.model.context.tracing.CustomTracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import io.opentracing.Scope;
import io.opentracing.Span;

/**
 * Service that enables Path Connectors to listen for events and respond to requests for data
 */
public abstract class RemoteService<T> implements MessageResponder, EventListener {

  // Statics

  private static final Logger LOGGER = LoggerFactory.getLogger(RemoteService.class);
  public static final Integer NANO_TO_MILLISECONDS = 1000000;

  // Fields

  private final Class<T> classOfT;
  private final String clientId;
  private final RequestContextHeaderForwarder requestContextHeaderForwarder;
  private Supplier<MessageBroker> messageBrokerSupplier;

  /// Constructor

  @SuppressWarnings("unchecked")
  public RemoteService(String clientId) {
    this.classOfT = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    this.clientId = clientId;
    this.requestContextHeaderForwarder = new RequestContextHeaderForwarder();
    this.messageBrokerSupplier = new FacilityMessageBrokerSupplier(clientId);
  }

  // Getter/Setters

  public final void setMessageBrokerSupplier(Supplier<MessageBroker> messageBrokerSupplier) {
    this.messageBrokerSupplier = messageBrokerSupplier;
  }

  // MessageResponder

  @Override
  public final String respond(String channel, String payloadJson) {
    MessageRequest messageRequest = MessageRequest.fromJson(payloadJson);
    MessageResponse messageResponse = dispatch(channel, messageRequest);

    return messageResponse.toJson();
  }

  // EventListener

  @Override
  public final void receive(String channel, String payload) {
    MessageEvent messageEvent = MessageEvent.fromJson(payload);
    dispatch(channel, messageEvent);
  }

  // Public

  /**
   * Implement to receive and process events
   *
   * @param channel      of event
   * @param messageEvent the event
   */
  public void dispatch(String channel, MessageEvent messageEvent) {
    LOGGER.info("Received event on " + channel);
    try {
      RemoteChannel remoteChannel = RemoteChannel.parse(channel);
      withTracing(remoteChannel, messageEvent, () -> {
        withSessionContext(remoteChannel, messageEvent, () -> {
          withDispatchMethod(remoteChannel, messageEvent);
          return null;
        });
      });
    } catch (Exception e) {
      LOGGER.error("Error processing event", e);
    }
  }

  /**
   * Implement to respond to messages
   *
   * <p>Default behavior finds implementation of operation matching:
   * {@code public MessageResponse [operation name](MessageRequest request)}
   *
   * @param channel        of request
   * @param messageRequest the request
   * @return MessageResponse
   */
  public MessageResponse dispatch(String channel, MessageRequest messageRequest) {
    RemoteChannel remoteChannel = RemoteChannel.parse(channel);
    return withTracing(remoteChannel, messageRequest, () -> {
      LOGGER.info("Received request on " + channel);
      try {
        return withSessionContext(remoteChannel, messageRequest,
            () -> withDispatchMethod(remoteChannel, messageRequest));

      } catch (MessageError e) {
        LOGGER.error("Error processing request", e);

        return MessageResponse.builder()
            .error(e.getMessage())
            .status(e.getMessageStatus())
            .exception(e)
            .build();
      } catch (Exception e) {
        LOGGER.error("Error processing request", e);

        return MessageResponse.builder()
            .error(e.getMessage())
            .status(MessageStatus.FAIL)
            .exception(e)
            .build();
      }
    });
  }

  /**
   * Register annotated service methods
   *
   * <p>Override to perform custom registration. Must override if dispatch is overriden.
   */
  public void register() {
    if (!implementsListenerDispatch()) {
      throw new InvalidServiceClassException("Service class (" + getClass().getSimpleName() + ") overrides listener dispatch without overriding register. If event dispatch override provided, must override register as well.");
    }

    if (!implementsRequestDispatch()) {
      throw new InvalidServiceClassException("Service class (" + getClass().getSimpleName() + ") overrides responder dispatch without overriding register. If request dispatch override provided, must override register as well.");
    }

    for (Method m : getClass().getMethods()) {
      Responder responderMethod = (Responder) m.getAnnotation(Responder.class);
      if (responderMethod != null) {
        registerResponder(m.getName());
      }

      Listener listenerMethod = (Listener) m.getAnnotation(Listener.class);
      if (listenerMethod != null) {
        registerListener(m.getName());
      }
    }
  }

  /**
   * Register a custom responder
   *
   * @param channel          to listen to
   * @param messageResponder implementation
   */
  public final void register(String channel, MessageResponder messageResponder) {
    messageBroker().registerResponder(channel, messageResponder);
  }

  /**
   * Register a custom events listener
   *
   * @param channel       to listen to
   * @param eventListener implementation of listener
   */
  public final void register(String channel, EventListener eventListener) {
    messageBroker().registerListener(channel, eventListener);
  }

  /**
   * Register this as a handler with given method name (event)
   *
   * @param event matching method name
   */
  public final void registerListener(String event) {
    try {
      if (implementsListenerDispatch()) {
        this.getClass().getMethod(event, String.class, MessageEvent.class);
      }
    } catch (NoSuchMethodException e) {
      throw new InvalidServiceClassException("Service class (" + getClass().getSimpleName() + ") does not implement listener for " + event + ". Check argument types and method name.");
    }

    messageBroker().registerListener(RemoteChannel.buildEventChannel(clientId, classOfT, event), this);
  }

  /**
   * Register this as a responder with given method name (operation)
   *
   * @param operation matching method name
   */
  public final void registerResponder(String operation) {
    try {
      if (implementsRequestDispatch()) {
        Method responderMethod = this.getClass().getMethod(operation, String.class, MessageRequest.class);
        if (responderMethod.getReturnType() != MessageResponse.class) {
          throw new InvalidServiceClassException("Service class (" + getClass().getSimpleName() + ") responder method (" + operation + ") has invalid return type. Must be MessageResponse.");
        }
      }
    } catch (NoSuchMethodException e) {
      throw new InvalidServiceClassException("Service class (" + getClass().getSimpleName() + ") does not implement responder for " + operation + ". Check argument types and method name.");
    }
    messageBroker().registerResponder(RemoteChannel.buildRequestChannel(clientId, classOfT, operation), this);
  }

  /**
   * Call in responder to check if session exists and is in authenticated state.
   *
   * <p>throws MessageError if session is not found or in bad state.
   */
  public final void requireSession() {
    if (Session.current() == null || Session.current().getSessionState() != Session.SessionState.AUTHENTICATED) {
      throw new MessageError("Invalid session", MessageStatus.NOT_AUTHORIZED, null);
    }
  }

  // Package Private

  /**
   * Inflates (and cleans up) the RequestContext and Session before forwarding the call to the designated handler.
   *
   * @param channel
   * @param message
   * @param f
   * @return MessageHeaders
   */
  MessageResponse withSessionContext(RemoteChannel channel, Object message, Supplier<MessageResponse> f) {
    MessageHeaders headers = getMessageHeaders(message);

    if (headers != null) {
      RequestContext requestContext = RequestContext.builder()
          .clientId(channel.getClientId())
          .path(channel.toString())
          .build();

      requestContextHeaderForwarder.extractFromMessageHeaders(requestContext, headers);

      requestContext.register();

      if (Strings.isNotBlank(headers.getSessionId())) {
        Session.loadSession(headers.getSessionId());
      }
    }

    try {
      return f.get();
    } finally {
      if (Session.current() != null) {
        Session.current().save();
      }
      Session.clearSession();
      RequestContext.clear();
    }
  }

  // Private

  private MessageHeaders getMessageHeaders(Object message) {
    MessageHeaders headers;
    if (message instanceof MessageRequest) {
      headers = ((MessageRequest) message).getMessageHeaders();
    } else if (message instanceof MessageEvent) {
      headers = ((MessageEvent) message).getMessageHeaders();
    } else {
      throw new IllegalArgumentException("message expected to be instance of MessageRequest or MessageEvent");
    }
    return headers;
  }

  /**
   * @return true, if child class overrides MessageEvent dispatch
   */
  private boolean implementsListenerDispatch() {
    try {
      Method dispatchMethod = this.getClass().getMethod("dispatch", String.class, MessageEvent.class);
      return dispatchMethod.getDeclaringClass() == RemoteService.class;
    } catch (NoSuchMethodException e) {
      throw new InvalidServiceClassException("Service class (" + getClass().getSimpleName() + ") has no listener dispatch method. This should never happen.", e);
    }
  }

  /**
   * @return true, if child class overrides MessageRequest dispatch
   */
  private boolean implementsRequestDispatch() {
    try {
      Method dispatchMethod = this.getClass().getMethod("dispatch", String.class, MessageRequest.class);
      return dispatchMethod.getDeclaringClass() == RemoteService.class;
    } catch (NoSuchMethodException e) {
      throw new InvalidServiceClassException("Service class (" + getClass().getSimpleName() + ") has no responder dispatch method. This should never happen.", e);
    }
  }

  private MessageBroker messageBroker() {
    if (messageBrokerSupplier == null) {
      throw new MessageError("No MessageBroker Supplier configured for RemoteService", MessageStatus.DISABLED, null);
    }
    return messageBrokerSupplier.get();
  }

  private void withDispatchMethod(RemoteChannel channel, MessageEvent messageEvent) {
    try {
      Method eventMethod = this.getClass().getMethod(channel.getOperation(), String.class, MessageEvent.class);
      long start = System.nanoTime();
      eventMethod.invoke(this, channel.toString(), messageEvent);
      long duration = (System.nanoTime() - start) / NANO_TO_MILLISECONDS;
      LOGGER.info("Processed event in " + duration + "ms");

    } catch (IllegalAccessException | ClassCastException e) {
      throw new MessageError("Event handler is not defined correctly", MessageStatus.INVALID_RESPONDER, e);
    } catch (MalformedChannelException | NoSuchMethodException e) {
      throw new MessageError("No event handler is defined for " + channel.getOperation(), MessageStatus.NO_RESPONDER, e);
    } catch (InvocationTargetException e) {
      Throwable ex = e;
      if (e.getCause() != null) {
        ex = e.getCause();
      }

      throw new MessageError("Event handler raised an exception: " + channel.getOperation(), MessageStatus.FAIL, ex);
    }
  }

  private MessageResponse withDispatchMethod(RemoteChannel channel, MessageRequest messageRequest) {
    MessageResponse response;
    try {
      Method eventMethod = this.getClass().getMethod(channel.getOperation(), String.class, MessageRequest.class);
      long start = System.nanoTime();
      response = (MessageResponse) eventMethod.invoke(this, channel.toString(), messageRequest);
      long duration = (System.nanoTime() - start) / NANO_TO_MILLISECONDS;
      LOGGER.info("Request response generated in " + duration + "ms");

    } catch (IllegalAccessException | ClassCastException e) {
      throw new MessageError("Responder is not defined correctly", MessageStatus.INVALID_RESPONDER, e);
    } catch (MalformedChannelException | NoSuchMethodException e) {
      throw new MessageError("No responder is defined for " + channel.getOperation(), MessageStatus.NO_RESPONDER, e);
    } catch (InvocationTargetException e) {
      Throwable ex = e;
      if (e.getCause() != null) {
        ex = e.getCause();
      }

      LOGGER.error("Responder raised an exception: " + channel.getOperation(), ex);
      throw new MessageError("Responder raised an exception: " + channel.getOperation(), getMessageStatus(ex), ex);
    }

    return response;
  }

  private MessageStatus getMessageStatus(Throwable ex) {
    MessageStatus messageStatus = MessageStatus.FAIL;

    if (ex.getClass() == MessageError.class) {
      return ((MessageError) ex).getMessageStatus();
    }
    return messageStatus;
  }

  private MessageResponse withTracing(RemoteChannel channel, MessageRequest message, Supplier<MessageResponse> f) {
    MDC.put("channel", channel.toString());
    MDC.put("operation", channel.getOperation());

    Span extractedSpan = RemoteTracePropagation.extract(message);
    try (Scope scope = CustomTracer.activateSpan(extractedSpan)) {
      return f.get();
    } finally {
      MDC.remove("channel");
      MDC.remove("operation");
      extractedSpan.finish();
    }
  }

  private void withTracing(RemoteChannel channel, MessageEvent message, Runnable f) {
    MDC.put("channel", channel.toString());
    MDC.put("operation", channel.getOperation());

    Span extractedSpan = RemoteTracePropagation.extract(message);
    try (Scope scope = CustomTracer.activateSpan(extractedSpan)) {
      f.run();
    } finally {
      MDC.remove("channel");
      MDC.remove("operation");
      extractedSpan.finish();
    }
  }
}
