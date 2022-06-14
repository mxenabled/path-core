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
import com.mx.accessors.Accessor;
import com.mx.accessors.AccessorResponse;
import com.mx.accessors.BaseAccessor;
import com.mx.common.collections.ObjectMap;
import com.mx.common.events.EventBus;
import com.mx.common.gateway.GatewayBaseClass;
import com.mx.path.api.connect.messaging.remote.RemoteService;
import com.mx.path.gateway.api.Gateway;
import com.mx.path.gateway.behavior.GatewayBehavior;
import com.mx.path.gateway.behavior.StartBehavior;
import com.mx.path.gateway.configuration.AccessorDescriber;
import com.mx.path.gateway.context.GatewayRequestContext;
import com.mx.path.gateway.events.AfterAccessorEvent;
import com.mx.path.gateway.events.BeforeAccessorEvent;
import com.mx.path.gateway.service.GatewayService;
import com.mx.path.model.context.RequestContext;
import com.mx.path.model.context.facility.Facilities;

@GatewayBaseClass(namespace = "com.mx.path.gateway.api", target = BaseAccessor.class, className = "Gateway", annotation = SuperBuilder.class)
@SuperBuilder
public abstract class BaseGateway {

  @Getter
  private String clientId;

  @Getter
  private BaseAccessor baseAccessor;

  @Setter
  private BaseGateway parent;

  @Getter
  @Setter
  private RemoteService<?> remote;

  @Getter
  @Singular
  private List<GatewayBehavior> behaviors = Collections.emptyList();

  @Getter
  @Singular
  private List<GatewayService> services;

  public BaseGateway() {
  }

  public BaseGateway(String clientId) {
    this.clientId = clientId;
  }

  /**
   * Registers all remote gateways
   */
  public void registerRemotes() {
    if (remote != null) {
      remote.register();
    }
    gateways().forEach(BaseGateway::registerRemotes);
  }

  /**
   * Start all services
   */
  public void startServices() {
    services.forEach(service -> {
      if (service.getGateway() == null) {
        service.setGateway(this);
      }
      service.start();
    });
    gateways().forEach(BaseGateway::startServices);
  }

  /**
   * Use reflection to discover all child gateways belonging to this
   *
   * Gateways must be exposed via a getter
   * @return List of BaseGateway
   */
  public ImmutableList<BaseGateway> gateways() {
    return ImmutableList.copyOf(Arrays.stream(getClass().getMethods())
        .filter(method -> BaseGateway.class.isAssignableFrom(method.getReturnType()))
        .filter(method -> method.getReturnType() != Gateway.class)
        .map(method -> {
          try {
            return (BaseGateway) method.invoke(this);
          } catch (InvocationTargetException | IllegalAccessException e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList()));
  }

  /**
   * Describe this gateway
   * @return description
   */
  public final ObjectMap describe() {
    ObjectMap description = new ObjectMap();
    describe(description);

    return description;
  }

  /**
   * Fill in description
   * Override and call super to get complete description.
   *
   * @param description
   */
  public void describe(ObjectMap description) {
    if (this instanceof Gateway) {
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
   * Generate description for all accessors in given
   * @param accessorToDescribe
   * @return
   */
  public void describeAccessors(Accessor accessorToDescribe, ObjectMap description) {
    AccessorDescriber accessorDescriber = new AccessorDescriber();
    accessorDescriber.describe(accessorToDescribe, description);
  }

  /**
   * Build the behavior call stack (decorator).
   *
   * todo: We had this setup as a lazy loaded singleton to avoid rebuilding it with every request.
   *       It was causing collisions. Not sure why. Just building fresh, with every request, for now.
   *       The collision exposed itself when behaviors "cross-called" other gateway actions.
   *       Seemed like the first gateway to execute had it's behavior set in stone. Doesn't seem like that should
   *       have happened. ¯\_(ツ)_/¯
   * @return
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

  protected final <T> AccessorResponse<T> executeBehaviorStack(Class<T> responseType, GatewayRequestContext request, GatewayBehavior terminatingBehavior) {
    return buildStack().execute(responseType, request, terminatingBehavior);
  }

  public final Gateway root() {
    if (parent != null) {
      return parent.root();
    }

    if (getClass().isAssignableFrom(Gateway.class)) {
      return (Gateway) this;
    }

    return null;
  }

  /**
   * Emit afterAccessorEvent
   * @param gateway
   * @param callingAccessor
   * @param requestContext
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
   * Emit beforeAccessorEvent
   * @param gateway
   * @param callingAccessor
   * @param requestContext
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
