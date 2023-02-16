package com.mx.path.gateway.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import lombok.Getter;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.mx.common.lang.TriConsumer;
import com.mx.path.gateway.Gateway;

/**
 * Observes the construction of gateways and receives events. Blocks and event listeners can be registered to
 * allow reactions to the events.
 *
 * <p>The following events are emitted
 *
 * <ul>
 *   <li>{@link ConfiguratorObserver.GatewaysInitializedEvent} - occurs once after all gateways have been initialized
 *   <li>{@link ConfiguratorObserver.GatewayInitializedEvent} - occurs once after each gateway is initialized
 *   <li>{@link ConfiguratorObserver.ClientGatewayInitializedEvent} - occurs once after each client's gateway stack is initialized
 * </ul>
 *
 * <p><b>Example:</b>
 * <pre>{@code
 *  // Generated configurator
 *  public class MdxConfigurator extends Configurator<MdxGateway> {
 *  }
 *
 *  try (MdxConfigurator configurator = new MdxConfigurator()) {
 *    configurator.getObserver().registerGatewaysInitialized((configurator, gateways) -> {
 *      // code to execute after all gateways are initialized
 *    });
 *
 *    // Build results in map of usable gateways with the clientId as the key.
 *    Map<String, MdxGateway> gateways =  configurator.buildFromYaml(yamlDocument);
 *  }
 * }</pre>
 */
public class ConfiguratorObserver {

  static class GatewaysInitializedEvent {
    @Getter
    private final Map<String, Gateway<?>> gateways;

    GatewaysInitializedEvent(Map<String, Gateway<?>> gateways) {
      this.gateways = gateways;
    }
  }

  static class GatewayInitializedEvent {
    @Getter
    private final Gateway<?> gateway;

    GatewayInitializedEvent(Gateway<?> gateway) {
      this.gateway = gateway;
    }
  }

  static class ClientGatewayInitializedEvent {
    @Getter
    private final String clientId;
    @Getter
    private final Gateway<?> gateway;

    ClientGatewayInitializedEvent(String clientId, Gateway<?> gateway) {
      this.clientId = clientId;
      this.gateway = gateway;
    }
  }

  private final List<BiConsumer<Configurator<?>, Map<String, Gateway<?>>>> gatewaysInitializedBlocks = new ArrayList<>();
  private final List<BiConsumer<Configurator<?>, Gateway<?>>> gatewayInitializedBlocks = new ArrayList<>();
  private final List<TriConsumer<Configurator<?>, String, Gateway<?>>> clientGatewayInitializedBlocks = new ArrayList<>();

  private final Configurator<?> configurator;
  private final EventBus eventBus;
  /**
   * A map of properties that is written by external observers during gateway construction
   */
  @Getter
  private final Map<String, String> properties = new HashMap<>();

  public ConfiguratorObserver(Configurator<?> configurator) {
    this.configurator = configurator;
    this.eventBus = new EventBus("configuratorObserver");
    eventBus.register(this);
  }

  @Subscribe
  final void gatewaysInitializedSubscriber(GatewaysInitializedEvent event) {
    gatewaysInitializedBlocks.forEach(consumer -> consumer.accept(configurator, event.gateways));
  }

  @Subscribe
  final void gatewayInitializedSubscriber(GatewayInitializedEvent event) {
    gatewayInitializedBlocks.forEach(consumer -> consumer.accept(configurator, event.gateway));
  }

  @Subscribe
  final void clientGatewayInitializedSubscriber(ClientGatewayInitializedEvent event) {
    clientGatewayInitializedBlocks.forEach(consumer -> consumer.accept(configurator, event.clientId, event.gateway));
  }

  final void notifyGatewaysInitialized(Map<String, Gateway<?>> gateways) {
    eventBus.post(new GatewaysInitializedEvent(gateways));
  }

  final void notifyGatewayInitialized(Gateway<?> gateway) {
    eventBus.post(new GatewayInitializedEvent(gateway));
  }

  final void notifyClientGatewayInitialized(String clientId, Gateway<?> gateway) {
    eventBus.post(new ClientGatewayInitializedEvent(clientId, gateway));
  }

  /**
   * Register an {@link EventBus} subscriber
   *
   * @param listener with {@link Subscribe} annotated functions
   */
  public final void registerListener(Object listener) {
    eventBus.register(listener);
  }

  /**
   * Register block of code to execute after all gateways initialized
   *
   * @param consumer block
   */
  public final void registerGatewaysInitialized(BiConsumer<Configurator<?>, Map<String, Gateway<?>>> consumer) {
    this.gatewaysInitializedBlocks.add(consumer);
  }

  /**
   * Register block of code to execute after each gateway is initialized
   *
   * @param consumer block
   */
  public final void registerGatewayInitialized(BiConsumer<Configurator<?>, Gateway<?>> consumer) {
    this.gatewayInitializedBlocks.add(consumer);
  }

  /**
   * Register block of code to execute after each client's gateways are initialized
   *
   * @param consumer block
   */
  public final void registerClientGatewayInitialized(TriConsumer<Configurator<?>, String, Gateway<?>> consumer) {
    this.clientGatewayInitializedBlocks.add(consumer);
  }
}
