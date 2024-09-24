package com.mx.path.gateway;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import com.google.common.collect.ImmutableList;
import com.mx.path.connect.messaging.remote.RemoteService;
import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.event.EventBus;
import com.mx.path.core.common.reflection.Annotations;
import com.mx.path.core.context.RequestContext;
import com.mx.path.core.context.facility.Facilities;
import com.mx.path.gateway.accessor.Accessor;
import com.mx.path.gateway.accessor.AccessorResponse;
import com.mx.path.gateway.behavior.GatewayBehavior;
import com.mx.path.gateway.behavior.StartBehavior;
import com.mx.path.gateway.configuration.AccessorDescriber;
import com.mx.path.gateway.configuration.RootGateway;
import com.mx.path.gateway.context.GatewayRequestContext;
import com.mx.path.gateway.event.AfterAccessorEvent;
import com.mx.path.gateway.event.BeforeAccessorEvent;
import com.mx.path.gateway.service.GatewayService;

/**
 * Gateway for accessor.
 *
 * @param <T> type of gateway
 */
@SuperBuilder
public abstract class Gateway<T extends Accessor> {

  /**
   * -- GETTER --
   * Return client id.
   *
   * @return client id
   */
  @Getter
  private String clientId;

  /**
   * -- GETTER --
   * Return gateway base accessor.
   *
   * @return base accessor
   */
  @Getter
  private T baseAccessor;

  /**
   * -- GETTER --
   * Return parent gateway.
   *
   * @return parent gateway
   */
  @Setter
  private Gateway parent;

  /**
   * -- GETTER --
   * Return gateway remote service.
   *
   * @return remote service
   * -- SETTER --
   * Set gateway remote service.
   *
   * @param remote remote service to set
   */
  @Getter
  @Setter
  private RemoteService<?> remote;

  /**
   * -- GETTER --
   * Return gateway behaviors.
   *
   * @return gateway behaviors
   */
  @Getter
  @Singular
  private List<GatewayBehavior> behaviors = Collections.emptyList();

  /**
   * -- GETTER --
   * Return gateway services.
   *
   * @return list of gateway services
   */
  @Getter
  @Singular
  private List<GatewayService> services;

  /**
   * Default constructor.
   */
  public Gateway() {
  }

  /**
   * Build new {@link Gateway} instance with specified client.
   *
   * @param clientId client id
   */
  public Gateway(String clientId) {
    this.clientId = clientId;
  }

  /**
   * Check if is root gateway.
   *
   * @return true if gateway is root
   */
  public final boolean isTopLevel() {
    return Annotations.hasAnnotation(getClass(), RootGateway.class);
  }

  /**
   * Use reflection to discover all child gateways belonging to this.
   *
   * <p>Gateways must be exposed via a getter
   *
   * @return List of BaseGateway
   */
  public ImmutableList<Gateway> gateways() {
    return ImmutableList.copyOf(Arrays.stream(getClass().getMethods())
        .filter(method -> Gateway.class.isAssignableFrom(method.getReturnType()))
        .filter(method -> method.getReturnType() != Gateway.class)
        .map(method -> {
          try {
            return (Gateway) method.invoke(this);
          } catch (InvocationTargetException | IllegalAccessException e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList()));
  }

  /**
   * Registers all remote gateways.
   */
  public void registerRemotes() {
    if (remote != null) {
      remote.register();
    }
    gateways().forEach(Gateway::registerRemotes);
  }

  /**
   * Start all services.
   */
  public void startServices() {
    services.forEach(service -> {
      if (service.getGateway() == null) {
        service.setGateway(this);
      }
      service.start();
    });
    gateways().forEach(Gateway::startServices);
  }

  /**
   * Describe this gateway.
   *
   * @return new description
   */
  public final ObjectMap describe() {
    ObjectMap description = new ObjectMap();
    describe(description);

    return description;
  }

  /**
   * Fill in description.
   *
   * <p>Override and call super to get complete description.
   *
   * @param description description object
   */
  public void describe(ObjectMap description) {
    if (isTopLevel()) {
      Facilities.describe(clientId, description.createMap("facilities"));
    } else {
      try {
        Method getAccessorMethod = this.getClass().getDeclaredMethod("getAccessor");
        Accessor accessor = (Accessor) getAccessorMethod.invoke(this);

        describeAccessors(accessor, description.createMap("accessor"));
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
      }
    }

    description.put("services", services.stream().map(GatewayService::describe).collect(Collectors.toList()));
    description.put("behaviors", behaviors.stream().map(GatewayBehavior::describe).collect(Collectors.toList()));

    if (baseAccessor == null) {
      throw new RuntimeException("Base accessor not provided to gateway " + getClass().getTypeName());
    }
  }

  /**
   * Generate description for all accessors in given.
   *
   * @param accessorToDescribe accessor to describe
   * @param description description being built
   */
  public void describeAccessors(Accessor accessorToDescribe, ObjectMap description) {
    AccessorDescriber accessorDescriber = new AccessorDescriber();
    accessorDescriber.describe(accessorToDescribe, description);
  }

  /**
   * Get gateway parent.
   *
   * @return parent
   * @param <T> gateway type
   */
  @SuppressWarnings("unchecked")
  public final <T extends Gateway> T getParent() {
    return (T) parent;
  }

  /**
   * Build the behavior call stack (decorator).
   *
   * todo: We had this setup as a lazy loaded singleton to avoid rebuilding it with every request.
   *       It was causing collisions. Not sure why. Just building fresh, with every request, for now.
   *       The collision exposed itself when behaviors "cross-called" other gateway actions.
   *       Seemed like the first gateway to execute had it's behavior set in stone. Doesn't seem like that should
   *       have happened. ¯\_(ツ)_/¯
   *
   * @return head of behavior stack
   */
  protected final GatewayBehavior buildStack() {
    GatewayBehavior stack = new StartBehavior();
    GatewayBehavior previous = stack;

    for (GatewayBehavior behavior : behaviors) {
      previous.setNextBehavior(behavior);
      previous = behavior;
    }

    return stack;
  }

  /**
   * Execute stack of behaviors.
   *
   * @param responseType type of response
   * @param request request
   * @param terminatingBehavior next behavior
   * @return response
   * @param <T> type of response
   */
  protected final <T> AccessorResponse<T> executeBehaviorStack(Class<T> responseType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
    return buildStack().execute(responseType, request, terminatingBehavior);
  }

  /**
   * Get root gateway.
   *
   * @return root gateway
   */
  public final Gateway root() {
    if (parent != null) {
      return parent.root();
    }

    return isTopLevel() ? this : null;
  }

  /**
   * Emit afterAccessorEvent.
   *
   * @param gateway current gateway
   * @param callingAccessor the current accessor
   * @param requestContext request context
   */
  public final void afterAccessor(Gateway gateway, Accessor callingAccessor, RequestContext requestContext) {
    if (requestContext.getClientId() == null) {
      return;
    }
    EventBus eventBus = Facilities.getEventBus(requestContext.getClientId());
    if (eventBus == null) {
      return;
    }

    eventBus.post(AfterAccessorEvent.builder()
        .currentAccessor(callingAccessor)
        .gateway(gateway)
        .requestContext(requestContext)
        .build());
  }

  /**
   * Emit beforeAccessorEvent.
   *
   * @param gateway current gateway
   * @param callingAccessor current accessor
   * @param requestContext request context
   */
  public final void beforeAccessor(Gateway gateway, Accessor callingAccessor, RequestContext requestContext) {
    if (requestContext.getClientId() == null) {
      return;
    }
    EventBus eventBus = Facilities.getEventBus(requestContext.getClientId());
    if (eventBus == null) {
      return;
    }

    eventBus.post(BeforeAccessorEvent.builder()
        .currentAccessor(callingAccessor)
        .gateway(gateway)
        .requestContext(requestContext)
        .build());
  }
}
