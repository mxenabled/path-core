package com.mx.path.gateway.configuration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.path.connect.messaging.remote.RemoteService;
import com.mx.path.core.common.collection.ObjectArray;
import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.event.EventBus;
import com.mx.path.core.common.exception.ExceptionReporter;
import com.mx.path.core.common.gateway.GatewayException;
import com.mx.path.core.common.lang.Strings;
import com.mx.path.core.common.messaging.MessageBroker;
import com.mx.path.core.common.process.FaultTolerantExecutor;
import com.mx.path.core.common.security.EncryptionService;
import com.mx.path.core.common.serialization.ObjectMapJsonDeserializer;
import com.mx.path.core.common.serialization.ObjectMapYamlDeserializer;
import com.mx.path.core.common.store.Store;
import com.mx.path.core.context.facility.Facilities;
import com.mx.path.core.utility.reflection.ClassHelper;
import com.mx.path.gateway.Gateway;
import com.mx.path.gateway.GatewayBuilderHelper;
import com.mx.path.gateway.accessor.Accessor;
import com.mx.path.gateway.behavior.GatewayBehavior;
import com.mx.path.gateway.event.GatewayEventBus;
import com.mx.path.gateway.service.GatewayService;

import org.apache.commons.text.StringSubstitutor;

/**
 * Base abstract class for Gateway Configurators.
 *
 * <p>Configures full Path stack from a gateway
 *
 * <p><b>Thread Safety</b>
 * <p>Instance is NOT thread-safe. Create a new configurator instance per thread if needed.
 *
 * <p><b>Example:</b>
 * <pre>{@code
 *   // Generated configurator
 *   public class MdxConfigurator extends Configurator<MdxGateway> {
 *   }
 *
 *   MdxConfigurator configurator = new MdxConfigurator();
 *
 *   // Build results in map of usable gateways with the clientId as the key.
 *   Map<String, MdxGateway> gateways =  configurator.buildFromYaml(yamlDocument);
 * }</pre>
 *
 * <p><b>Lifecycle events</b>
 * <p>See {@link ConfiguratorObserver}</p>
 */
public abstract class Configurator<T extends Gateway<?>> {
  private static final int MAX_YAML_ALIASES = 100;

  private final Stack<Accessor> accessorStack = new Stack<>();
  private final ConfigurationState state = ConfigurationState.getCurrent();
  private final AccessorStackConfigurator accessorConfigurator = new AccessorStackConfigurator(state);
  private final BehaviorStackConfigurator behaviorStackConfigurator = new BehaviorStackConfigurator(state);
  private final GatewayObjectConfigurator gatewayObjectConfigurator = new GatewayObjectConfigurator(state);
  @Getter
  private final Class<T> rootGatewayClass;
  @Getter
  @Setter(AccessLevel.PACKAGE)
  private ConfiguratorObserver<T> observer;

  // Constructors

  @SuppressWarnings("unchecked")
  public Configurator() {
    this.rootGatewayClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    this.observer = new ConfiguratorObserver<T>(this);
  }

  /**
   * Build gateway from a configuration ObjectMap
   *
   * @param map ObjectMap of configuration
   * @param clientId Client ID
   * @return Configured gateway
   */
  public final T buildGateway(ObjectMap map, String clientId) {
    behaviorStackConfigurator.setRootBehaviors(map.getArray("rootBehaviors"));

    populateFacilities(clientId, map);
    T gateway = buildGateway("root", map, clientId, GatewayBuilderHelper.getBuilder(rootGatewayClass));

    getObserver().notifyClientGatewayInitialized(clientId, gateway);

    return gateway;
  }

  /**
   * Build gateway from json string
   *
   * @param json string
   * @return T
   */
  public final Map<String, T> buildFromJson(String json) {
    GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(ObjectMap.class, new ObjectMapJsonDeserializer());
    Gson gson = gsonBuilder.create();

    json = StringSubstitutor.createInterpolator().replace(json);
    ObjectMap map = gson.fromJson(json, ObjectMap.class);

    return buildGateways(map);
  }

  public final Map<String, T> buildFromYaml(String document) {
    if (Strings.isBlank(document)) {
      return new LinkedHashMap<>();
    }
    document = StringSubstitutor.createInterpolator().replace(document);

    ObjectMapYamlDeserializer yamlSerializer = new ObjectMapYamlDeserializer(ObjectMapYamlDeserializer.Parameters.builder()
        .maxYamlAliases(MAX_YAML_ALIASES)
        .build());
    Object root = yamlSerializer.fromYaml(document);

    if (!(root instanceof ObjectMap)) {
      throw new RuntimeException("buildFromYaml requires an object root.");
    }

    ObjectMap map = (ObjectMap) root;

    // We allow a definitions block to place common config; however, after the YAML
    // has been parsed we are good to remove it since all references have already been
    // populated.
    if (map != null && map.containsKey("definitions")) {
      map.remove("definitions");
    }

    return buildGateways(map);
  }

  private void buildBehaviors(ObjectMap map, Object builder, String clientId) {
    List<GatewayBehavior> behaviors = behaviorStackConfigurator.buildFromNode(map, clientId);
    behaviors.forEach(behavior -> GatewayBuilderHelper.addBehavior(builder, behavior));
  }

  private Map<String, T> buildGateways(ObjectMap map) {
    Map<String, T> result = new LinkedHashMap<>();

    if (map != null) {
      for (String clientName : map.keySet()) {
        result.put(clientName, (T) buildGateway(map.getMap(clientName), clientName));
      }
    }

    getObserver().notifyGatewaysInitialized(result);

    return result;
  }

  private <G extends Gateway<?>> G buildGateway(String name, ObjectMap map, String clientId, Object builder) {
    GatewayBuilderHelper.setClientId(builder, clientId);

    buildBehaviors(map, builder, clientId);
    buildServices(map, builder, clientId);
    Accessor parent = null;
    if (!accessorStack.isEmpty()) {
      parent = accessorStack.peek();
    }
    accessorStack.push(accessorConfigurator.buildAccessor(name, map, clientId, builder, parent));

    buildGateways(map, clientId, builder);

    accessorStack.pop();

    Gateway<?> gateway = GatewayBuilderHelper.build(builder, Gateway.class);

    if (map.getMap("remotes") != null) {
      buildRemote(map.getMap("remotes"), gateway, clientId);
    }

    getObserver().notifyGatewayInitialized(gateway);

    return (G) gateway;
  }

  private void buildRemote(ObjectMap configurations, Gateway gateway, String clientId) {
    try {
      // The remote gateway package structure mirrors the normal gateway package structure with the caveat that `api`
      // is changed to `remote`. So we need to turn `com.mx.path.gateway.api.account.AccountGateway` into
      // `com.mx.path.gateway.remote.account.RemoteAccountGateway` so that we can successfully load the class.
      String remoteGatewayClassName = gateway.getClass().getPackage().getName().replace("api", "remote") + ".Remote" + gateway.getClass().getSimpleName();
      Class<? extends RemoteService<?>> remoteGatewayClass = (Class<? extends RemoteService<?>>) new ClassHelper().getClass(remoteGatewayClassName);
      Constructor<? extends RemoteService<?>> constructor = remoteGatewayClass.getConstructor(String.class, gateway.getClass(), ObjectMap.class);
      RemoteService<?> remote = constructor.newInstance(clientId, gateway, configurations);
      gateway.setRemote(remote);
    } catch (ClassCastException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new GatewayException("Unable to create remote gateway " + gateway.getClass().getSimpleName(), e);
    }
  }

  /**
   * Build sub-gateways
   *
   * @param map definition
   * @param clientId of owning client
   * @param builder  Current GatewayBuilder
   */
  private void buildGateways(ObjectMap map, String clientId, Object builder) {
    ObjectMap gatewaysNode = map.getMap("gateways");
    if (gatewaysNode == null) {
      return;
    }

    Arrays.stream(builder.getClass().getMethods()).filter(m -> {
      if (m.getParameterCount() == 1) {
        return Gateway.class.isAssignableFrom(m.getParameters()[0].getType());
      }

      return false;
    }).forEach(m -> {
      if (gatewaysNode.containsKey(m.getName())) {
        try {
          Object gatewayBuilder = m.getParameters()[0].getType().getMethod("builder").invoke(m.getParameters()[0].getType());
          Object gateway = buildGateway(m.getName(), gatewaysNode.getMap(m.getName()), clientId, gatewayBuilder);
          m.invoke(builder, gateway);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
          throw new GatewayException("Unable to add gateway API to gateway " + m.getName(), e);
        }
      }
    });
  }

  private void buildServices(ObjectMap map, Object builder, String clientId) {
    ObjectArray services = map.getArray("services");
    if (services == null) {
      return;
    }

    state.withLevel("services", () -> {
      services.forEach((node) -> {
        GatewayBuilderHelper.addService(builder, gatewayObjectConfigurator.buildFromNode((ObjectMap) node, clientId, GatewayService.class));
      });
    });
  }

  private void populateFacilities(String clientId, ObjectMap map) {
    ObjectMap node = map.getMap("facilities");
    if (node == null) {
      return;
    }

    state.withLevel("facilities", () -> {
      node.keySet().forEach(key -> {
        switch (key) {
          case "cacheStore":
            Facilities.setCacheStore(clientId, gatewayObjectConfigurator.buildFromNode(node.getMap(key), clientId, Store.class));
            break;

          case "encryptionService":
            Facilities.setEncryptionService(clientId, gatewayObjectConfigurator.buildFromNode(node.getMap(key), clientId, EncryptionService.class));
            break;

          case "eventBus":
            Facilities.addEventBus(clientId, gatewayObjectConfigurator.buildFromNode(node.getMap(key), clientId, EventBus.class));
            break;

          case "exceptionReporter":
            Facilities.setExceptionReporter(clientId, gatewayObjectConfigurator.buildFromNode(node.getMap(key), clientId, ExceptionReporter.class));
            break;

          case "faultTolerantExecutor":
            Facilities.setFaultTolerantExecutor(clientId, gatewayObjectConfigurator.buildFromNode(node.getMap(key), clientId, FaultTolerantExecutor.class));
            break;

          case "messageBroker":
            Facilities.setMessageBroker(clientId, gatewayObjectConfigurator.buildFromNode(node.getMap(key), clientId, MessageBroker.class));
            break;

          case "sessionStore":
            Facilities.setSessionStore(clientId, gatewayObjectConfigurator.buildFromNode(node.getMap(key), clientId, Store.class));
            break;

          case "secretStore":
            Facilities.setSecretStore(clientId, gatewayObjectConfigurator.buildFromNode(node.getMap(key), clientId, Store.class));
            break;

          default:
            throw new GatewayException("Invalid facility: " + key);
        }
      });
    });

    ensureDefaultFacilities(clientId);

    getObserver().notifyClientFacilitiesInitialized(clientId);
  }

  private void ensureDefaultFacilities(String clientId) {
    //Default Event Bus
    if (Facilities.getEventBus(clientId) == null) {
      Facilities.addEventBus(
          clientId,
          new GatewayEventBus(new ObjectMap()));
    }

    //Future defaults added here **
  }
}
