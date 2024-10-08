package com.mx.path.gateway.event;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.google.common.eventbus.Subscribe;
import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.event.EventBus;
import com.mx.path.core.common.reflection.Annotations;
import com.mx.path.gateway.util.UpstreamRequestLoggingEventListener;

/**
 * Configured per client through gateway.yaml facilities.
 *
 * <p>When this facility is present in the gateway configuration
 * The gateway will emit events to it for the client.
 *
 * <p>Wraps Google's Guava EventBus
 *
 * <p>configurations:
 *   <ul>
 *     <li>NO OPTIONS</li>
 *   </ul>
 */
public class GatewayEventBus implements EventBus {

  private final com.google.common.eventbus.EventBus eventBus;

  /**
   * Build {@link GatewayEventBus} with specified configuration.
   *
   * @param configuration configuration
   */
  @SuppressWarnings("PMD.UnusedFormalParameter")
  public GatewayEventBus(ObjectMap configuration) {
    eventBus = new com.google.common.eventbus.EventBus();
    eventBus.register(new DefaultEventHandler());
    // todo: Move this registration somewhere better?
    eventBus.register(new UpstreamRequestLoggingEventListener());
  }

  /**
   * Post an event on the event bus
   *
   * <p>The event type must meet the following criteria
   * <ul>
   *   <li>The event must implement {@link GatewayEvent} and/or {@link AccessorEvent}</li>
   * </ul>
   *
   * <p><i>Notes:</i>
   * @param event event
   */
  @Override
  public void post(Object event) {
    eventBus.post(event);
  }

  /**
   * Register an event subscriber
   *
   * <p>The class must have at least one method that meets the following criteria:
   * <ul>
   *   <li>Is annotated with {@link Subscribe}</li>
   *   <li>Accepts one (and only one) parameter whose Type matches the event being subscribed to</li>
   *   <li>The event type must implement {@link GatewayEvent} and/or {@link AccessorEvent}</li>
   *   <li>The event methods <i>must not</i> throw exceptions</li>
   * </ul>
   *
   * <p><i>Notes:</i>
   * <p>The subscriber class can have more than one {@link Subscribe} annotated method
   * <p>The class and its subscriber methods must be thread-safe since the subscriber is a singleton</p>
   * <p>Subscribers should not be used for side effects that change the flow of a request. Subscribes are meant to handle small,
   *    maintenance or cross-cutting tasks (e.g. Logging, Metrics, Cleanup)</p>
   * <p>The subscribers for a posted event are executed synchronously and the order is indeterminate. Take care not to execute
   *    long running code or code that calls large, unknown code branches to avoid recursive event triggering.
   *
   * @param subscriber to register
   */
  @Override
  public void register(Object subscriber) {
    checkSubscriber(subscriber);
    eventBus.register(subscriber);
  }

  private void checkSubscriber(Object subscriber) {
    List<Method> subscribeMethods = Annotations.methodsWithAnnotation(Subscribe.class, subscriber.getClass());
    if (subscribeMethods.size() == 0) {
      throw new GatewayEventBusException("Invalid event bus subscriber class - " + subscriber.getClass() + " has no methods annotated @Subscriber");
    }

    subscribeMethods.forEach(method -> {
      Class<?>[] parameters = method.getParameterTypes();
      Arrays.asList(parameters).forEach(parameterClass -> {
        if (!GatewayEvent.class.isAssignableFrom(parameterClass)
            && !AccessorEvent.class.isAssignableFrom(parameterClass)
            && !UpstreamRequestEvent.class.isAssignableFrom(parameterClass)) {
          throw new GatewayEventBusException("Invalid event bus subscriber - " + subscriber.getClass().getCanonicalName() + "." + method.getName() + " handles event type " + parameterClass.getCanonicalName() + " which does not implement GatewayEvent, AccessorEvent, or UpstreamRequestEvent");
        }
      });
    });
  }

}
