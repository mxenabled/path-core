package com.mx.path.gateway.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import lombok.Getter;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.mx.path.core.common.lang.TriConsumer;
import com.mx.path.gateway.Gateway;

/**
 * Observes the construction of gateways and receives events. Blocks and event listeners can be registered to
 * allow reactions to the events.
 *
 * <p>The following events are emitted
 *
 * <ul>
 *   <li>{@link ConfiguratorObserver.GatewayInitializedEvent} - occurs once after each gateway is initialized
 *   <li>{@link ConfiguratorObserver.GatewaysInitializedEvent} - occurs once after all gateways have been initialized
 *   <li>{@link ConfiguratorObserver.ClientGatewayInitializedEvent} - occurs once after each client's gateway stack is initialized
 *   <li>{@link ConfiguratorObserver.ClientFacilitiesInitializedEvent} - occurs once after each client's facilities are initialized
 * </ul>
 *
 * <p><b><i>Registering observer blocks (preferred)</i></b>
 *
 * <p><i>Example:</i>
 * <pre>{@code
 *
 *   // Generated configurator
 *   public class MdxConfigurator extends Configurator<MdxGateway> {
 *   }
 *
 *   MdxConfigurator configurator = new MdxConfigurator();
 *   configurator.getObserver().registerGatewaysInitialized((configurator, gateways) -> {
 *     // code to execute after all gateways are initialized
 *   });
 *
 *   // Build results in map of usable gateways with the clientId as the key.
 *   Map<String, MdxGateway> gateways =  configurator.buildFromYaml(yamlDocument);
 *
 * }</pre>
 *
 * <p><b>Note:</b> Every block registered will be invoked, even if they are identical. There is no concept of a
 * <i>run-only-once</i> when using blocks.
 *
 * <p><b><i>Registering listener</i></b>
 *
 * <p>This can be used to implement a run-only-once listener
 *
 * <p><i>Example:</i>
 * <pre>{@code
 *
 *   // Generated configurator
 *   public class MdxConfigurator extends Configurator<MdxGateway> {
 *   }
 *
 *   public class CustomListener {
 *     @Subscribe
 *     public void gatewaysInitialized(GatewaysInitializedEvent event) {
 *       // code to execute after all gateways are initialized
 *     }
 *   }
 *
 *   // Use same instance
 *   CustomListener listener = new CustomListener();
 *
 *   MdxConfigurator configurator = new MdxConfigurator();
 *
 *   configurator.getObserver().registerListener(listener);
 *   configurator.getObserver().registerListener(listener);
 *
 *   Map<String, MdxGateway> gateways =  configurator.buildFromYaml(yamlDocument);
 *   // listener.gatewaysInitialized(...) will only be invoked once, because the listener was the same instance.
 *
 * }</pre>
 */
public class ConfiguratorObserver<G extends Gateway<?>> {

  public static class ClientFacilitiesInitializedEvent<T extends Gateway<?>> {
    @Getter
    private final Configurator<T> configurator;
    @Getter
    private final String clientId;

    public ClientFacilitiesInitializedEvent(Configurator<T> configurator, String clientId) {
      this.configurator = configurator;
      this.clientId = clientId;
    }
  }

  public static class GatewayInitializedEvent<T extends Gateway<?>> {
    @Getter
    private final Configurator<T> configurator;
    @Getter
    private final Gateway<?> gateway;

    public GatewayInitializedEvent(Configurator<T> configurator, Gateway<?> gateway) {
      this.configurator = configurator;
      this.gateway = gateway;
    }
  }

  public static class GatewaysInitializedEvent<T extends Gateway<?>> {
    @Getter
    private final Configurator<T> configurator;
    @Getter
    private final Map<String, T> gateways;

    public GatewaysInitializedEvent(Configurator<T> configurator, Map<String, T> gateways) {
      this.configurator = configurator;
      this.gateways = gateways;
    }
  }

  public static class ClientGatewayInitializedEvent<T extends Gateway<?>> {
    @Getter
    private final Configurator<T> configurator;
    @Getter
    private final String clientId;
    @Getter
    private final T gateway;

    public ClientGatewayInitializedEvent(Configurator<T> configurator, String clientId, T gateway) {
      this.configurator = configurator;
      this.clientId = clientId;
      this.gateway = gateway;
    }
  }

  private final List<BiConsumer<Configurator<G>, String>> clientFacilitiesInitializedBlocks = new ArrayList<>();
  private final List<TriConsumer<Configurator<G>, String, G>> clientGatewayInitializedBlocks = new ArrayList<>();
  private final List<BiConsumer<Configurator<G>, Gateway<?>>> gatewayInitializedBlocks = new ArrayList<>();
  private final List<BiConsumer<Configurator<G>, Map<String, G>>> gatewaysInitializedBlocks = new ArrayList<>();

  private final Configurator<G> configurator;
  private final EventBus eventBus;
  /**
   * A map of properties that is written by external observers during gateway construction
   */
  @Getter
  private final Map<String, String> properties = new HashMap<>();

  public ConfiguratorObserver(Configurator<G> configurator) {
    this.configurator = configurator;
    this.eventBus = new EventBus("configuratorObserver");
    eventBus.register(this);
  }

  @Subscribe
  final void clientFacilitiesInitializedSubscriber(ClientFacilitiesInitializedEvent<G> event) {
    clientFacilitiesInitializedBlocks.forEach(consumer -> consumer.accept(event.configurator, event.clientId));
  }

  @Subscribe
  final void clientGatewayInitializedSubscriber(ClientGatewayInitializedEvent<G> event) {
    clientGatewayInitializedBlocks.forEach(consumer -> consumer.accept(event.configurator, event.clientId, event.gateway));
  }

  @Subscribe
  final void gatewayInitializedSubscriber(GatewayInitializedEvent<G> event) {
    gatewayInitializedBlocks.forEach(consumer -> consumer.accept(event.configurator, event.gateway));
  }

  @Subscribe
  final void gatewaysInitializedSubscriber(GatewaysInitializedEvent<G> event) {
    gatewaysInitializedBlocks.forEach(consumer -> consumer.accept(event.configurator, event.gateways));
  }

  final void notifyClientFacilitiesInitialized(String clientId) {
    eventBus.post(new ClientFacilitiesInitializedEvent<G>(configurator, clientId));
  }

  final void notifyClientGatewayInitialized(String clientId, G gateway) {
    eventBus.post(new ClientGatewayInitializedEvent<G>(configurator, clientId, gateway));
  }

  final void notifyGatewayInitialized(Gateway<?> gateway) {
    eventBus.post(new GatewayInitializedEvent<G>(configurator, gateway));
  }

  final void notifyGatewaysInitialized(Map<String, G> gateways) {
    eventBus.post(new GatewaysInitializedEvent<G>(configurator, gateways));
  }

  /**
   * Register an {@link EventBus} subscriber
   *
   * <p>This allows for the registration of an external observer. This will allow for registration of a singleton
   * observer multiple times that will only receive the events once. See class docs.
   *
   * @param listener with {@link Subscribe} annotated functions
   */
  public final void registerListener(Object listener) {
    eventBus.register(listener);
  }

  /**
   * Register block of code to execute after each client's facilities are initialized
   *
   * @param consumer block
   */
  public final void registerClientFacilitiesInitialized(BiConsumer<Configurator<G>, String> consumer) {
    this.clientFacilitiesInitializedBlocks.add(consumer);
  }

  /**
   * Register block of code to execute after each client's gateways are initialized
   *
   * @param consumer block
   */
  public final void registerClientGatewayInitialized(TriConsumer<Configurator<G>, String, G> consumer) {
    this.clientGatewayInitializedBlocks.add(consumer);
  }

  /**
   * Register block of code to execute after each gateway is initialized
   *
   * @param consumer block
   */
  public final void registerGatewayInitialized(BiConsumer<Configurator<G>, Gateway<?>> consumer) {
    this.gatewayInitializedBlocks.add(consumer);
  }

  /**
   * Register block of code to execute after all gateways initialized
   *
   * @param consumer block
   */
  public final void registerGatewaysInitialized(BiConsumer<Configurator<G>, Map<String, G>> consumer) {
    this.gatewaysInitializedBlocks.add(consumer);
  }
}
